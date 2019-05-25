package component.learning

import com.cra.figaro.algorithm.learning.{EMWithBP, GeneralizedEM}
import component.soccer.TeamHelper
import component.{LearningModel, Model, PostParameters, PriorParameters}
import domain.learning.LearningResponse
import domain.soccer.{Fixture, Form, Rating, Team, TeamProbability}
import services.soccer.{FixtureService, FormService, RatingService, TeamProbabilityService, TeamService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class LearningEngine {

  def getWinsnLoses(teamH2H: Seq[Fixture]): (Seq[Fixture], Seq[Fixture]) = {
    (teamH2H.filter(h2h => h2h.homeTeamGoals >= h2h.awayTeamGoals),
      teamH2H.filter(h2h => h2h.awayTeamGoals > h2h.homeTeamGoals))
  }

  def getGoodnBadRatings(teamRatings: Seq[Rating]): (Seq[Rating], Seq[Rating]) =
    (teamRatings.filter(r => r.rating > 6.9), teamRatings.filter(r => r.rating <= 6.9))

  def getRatingLabels(teamRatings: Seq[Rating]): Seq[Option[Boolean]] = {
    val oneThird = teamRatings.size / 3
    val ratingLabelMaxLength = if (oneThird == 0) teamRatings.size else oneThird

    val (goodrating, badrating) = getGoodnBadRatings(teamRatings)

    val (labeledGoodRatings, labeledBadRatings) = labelData(goodrating, badrating, ratingLabelMaxLength)

    for {
      rating <- teamRatings
    } yield {
      if (labeledGoodRatings.contains(rating)) Some(true)
      else if (labeledBadRatings.contains(rating)) Some(false)
      else None
    }
  }

  def getFixtureLabels(teamH2H: Seq[Fixture]): Seq[Option[Boolean]] = {
    val oneThird = teamH2H.size / 3
    val fixtureLabelMaxLength = if (oneThird == 0) teamH2H.size else oneThird

    val (wins, loses) = getWinsnLoses(teamH2H)

    val oneThirdWins = wins.size / 3
    val winLength = if (oneThirdWins == 0) wins.size else oneThirdWins
    val winLabelSize = if (winLength < fixtureLabelMaxLength) fixtureLabelMaxLength else winLength

    val oneThirdLoses = loses.size / 3
    val loseLength = if (oneThirdLoses == 0) loses.size else oneThirdLoses
    val losesLabelSize = if (loseLength < fixtureLabelMaxLength) fixtureLabelMaxLength else loseLength


    val labeledWins = wins.take(winLabelSize)
    val labeledLoses = loses.take(losesLabelSize)

    for {
      fixture <- teamH2H
    } yield {
      if (labeledWins.contains(fixture)) Some(true)
      else if (labeledLoses.contains(fixture)) Some(false)
      else None
    }
  }

  def labelData[A](positive: Seq[A], negative: Seq[A], maxSize: Int): (Seq[A], Seq[A]) = {
    val oneThirdPositive = positive.size / 3
    val positiveSize = if (oneThirdPositive == 0) positive.size else oneThirdPositive
    val positiveLabelSize = if (positiveSize < maxSize) maxSize else positiveSize

    val oneThirdNegative = negative.size / 3
    val negativeSize = if (oneThirdNegative == 0) negative.size else oneThirdNegative
    val negativeLabelSize = if (negativeSize < maxSize) maxSize else negativeSize

    val labeledPositives = positive.take(positiveLabelSize)
    val labeledNegatives = negative.take(negativeLabelSize)
    (labeledPositives, labeledNegatives)
  }

  def getGoodnBadForms(teamForms: Seq[Form]): (Seq[Form], Seq[Form]) =
    (teamForms.filter(form => form.numberOfWins + form.numberOfDraws / 2 > form.numberOfLoses + form.numberOfDraws / 2),
      teamForms.filter(form => form.numberOfLoses + form.numberOfDraws / 2 > form.numberOfWins + form.numberOfDraws / 2))

  def getFormLabels(teamForms: Seq[Form]): Seq[Option[Boolean]] = {
    val oneThird = teamForms.size / 3
    val formLabelMaxLength = if (oneThird == 0) teamForms.size else oneThird

    val (goodForms, badForms) = getGoodnBadForms(teamForms)

    val (labeledGoodForms, labeledBadForms) = labelData(goodForms, badForms, formLabelMaxLength)

    for {
      form <- teamForms
    } yield {
      if (labeledGoodForms.contains(form)) Some(true)
      else if (labeledBadForms.contains(form)) Some(false)
      else None
    }
  }

  def learnOnLabels(fixtureLabel: Option[Boolean], formLabel: Option[Boolean], ratingLabel: Option[Boolean], priorParams: PriorParameters): Model = {
    val model: Model = new LearningModel(priorParams)
    new TeamHelper().observeEvidence(model, fixtureLabel, formLabel, ratingLabel)
    //    println("model for form::: ", model)
    //    println()
    model
  }

  def learnModels(teamH2H: Seq[Fixture], teamForms: Seq[Form], teamRatings: Seq[Rating], target: String, priorParams: PriorParameters): Seq[Model] = {

    val teamId = teamForms.head.teamId

    println("Team Fixtures for team with id: " + teamId + " = " + teamH2H)
    println()
    println("Team Forms for team with id: " + teamId + " = " + teamForms)
    println()
    println("Team Ratings for team with id: " + teamId + " = " + teamRatings)
    println()

    val fixtureLabels = getFixtureLabels(teamH2H)
    val formLabels = getFormLabels(teamForms)
    val ratingLabels = getRatingLabels(teamRatings)

    for {
      fixtureLabel <- fixtureLabels
      formLabel <- formLabels
      ratingLabel <- ratingLabels
    } yield {
      learnOnLabels(fixtureLabel, formLabel, ratingLabel, priorParams)
    }

  }

  def saveGlobal(teamProbabilities: TeamProbability): Future[Boolean] =
    for {
      batchSave <- TeamProbabilityService.batchViewImpl.saveEntity(teamProbabilities)
      rtSave <- TeamProbabilityService.realtimeViewImpl.saveEntity(teamProbabilities)
    } yield {
      if (batchSave) println("Saved in batch db")
      if (rtSave) println("Saved in real time db")
      batchSave && rtSave
    }

  def saveResult(teamProbabilities: TeamProbability, target: String): Future[Boolean] =
    if (target.equals(LearningEngine.BATCH))
      saveGlobal(teamProbabilities)
    else
      for {
        rtSave <- TeamProbabilityService.realtimeViewImpl.saveEntity(teamProbabilities)
      } yield rtSave

  def cleanRT(team: Team): Future[Boolean] = for {
    clearRating <- RatingService.pseudomasterImpl.deleteByTeamId(team.teamId)
    clearFixture <- FixtureService.pseudomasterImpl.deleteByTeamId(team.teamId)
    clearForm <- FormService.pseudomasterImpl.deleteByTeamId(team.teamId)
  } yield {
    if (clearFixture) println("RT Fixtures cleared!")
    if (clearForm) println("RT Forms cleared!")
    if (clearRating) println("RT Ratings cleared!")
    clearFixture && clearForm && clearRating
  }

  def learnMAP(priorParams: PriorParameters, algo: GeneralizedEM): (PostParameters) = {
//        val algorithm = LearningEngine.ALGORITHM
    //    val algorithm = EMWithVE(PARAMS.probabilities: _*)
//    LearningEngine.startAlgorithm()
//    println("In learnMAP, is algo active? ", ALGORITHM.isActive)
    println(s"Is algo ${algo} for param ${priorParams} active? ${algo.isActive}")
    if (!algo.isActive) algo.start()
    println(s"How about now? ${algo.isActive}")

    val winProbability = priorParams.winProbability.MAPValue
    val goodRatingProbability = priorParams.goodRatingProbability.MAPValue
    val badRatingProbability = priorParams.badRatingProbability.MAPValue
    val goodFormProbability = priorParams.goodFormProbability.MAPValue
    val badFormProbability = priorParams.badFormProbability.MAPValue
    val goodHead2HeadProbability = priorParams.goodHead2HeadProbability.MAPValue
    val badHead2HeadProbability = priorParams.badHead2HeadProbability.MAPValue
//    algo.kill()
    println(s"Killing algo for param: ${priorParams}...")
    algo.kill();
    println("Algo killed...returning response...")
    (new PostParameters(winProbability, goodRatingProbability, badRatingProbability, goodFormProbability, badFormProbability, goodHead2HeadProbability, badHead2HeadProbability))
  }

  def stepTwo(team: Team, fixtures: Seq[Fixture], forms: Seq[Form], ratings: Seq[Rating], target: String): TeamProbability = {
    val priorParams = new PriorParameters
    val models = learnModels(fixtures, forms, ratings, target, priorParams)
    println(models)
    println()
    val algo = EMWithBP(priorParams.probabilities: _*)
    println(s"${team.teamName} has this prior params: ${priorParams} and this algo: ${algo}")
    val (result) = learnMAP(priorParams, algo)
    println(s"Is algo ${algo} active?: ", algo.isActive)
    TeamProbability(team.teamId, result.winProbability, result.goodRatingProbability, result.badRatingProbability,
      result.goodFormProbability, result.badFormProbability, result.goodHead2HeadProbability, result.badHead2HeadProbability)
  }

  def stepOne(team: Team, fixtures: Seq[Fixture], forms: Seq[Form], ratings: Seq[Rating], target: String): LearningResponse = {
    val teamProbabilities = stepTwo(team, fixtures, forms, ratings, target)
    saveResult(teamProbabilities, target)
    if (target.equals(LearningEngine.BATCH)) cleanRT(team)
    new LearningResponse(true, "Learning completed for team with id: " + team.teamId, teamProbabilities)
  }

  def getTeamMatches(teamId: String, target: String): Future[Seq[Fixture]] =
    if (target.equals(LearningEngine.BATCH)) FixtureService.masterImpl.getHomeTeamMatches(teamId)
    else FixtureService.pseudomasterImpl.getHomeTeamMatches(teamId)

  def getTeamForms(teamId: String, target: String): Future[Seq[Form]] =
    if (target.equals(LearningEngine.BATCH)) FormService.masterImpl.getTeamForms(teamId)
    else FormService.pseudomasterImpl.getTeamForms(teamId)

  def getTeamRatings(teamId: String, target: String): Future[Seq[Rating]] =
    if (target.equals(LearningEngine.BATCH)) RatingService.masterImpl.getTeamRatings(teamId)
    else RatingService.pseudomasterImpl.getTeamRatings(teamId)

  def getTeamStat(team: Team, target: String): Future[(Seq[Fixture], Seq[Form], Seq[Rating])] = {
    for {
      teamH2H <- getTeamMatches(team.teamId, target)
      teamForms <- getTeamForms(team.teamId, target)
      teamRatings <- getTeamRatings(team.teamId, target)
    } yield {
      (teamH2H, teamForms, teamRatings)
    }
  }

  def processLearn(team: Option[Team], id: String, target: String): Future[LearningResponse] = {
    team match {
      case Some(t) =>
        println("Learning for team: " + t.teamName + "..." + " | Team ID: " + t.teamId)
        println()
        for {
          (fixtures, forms, ratings) <- getTeamStat(t, target)
        } yield {
          stepOne(t, fixtures, forms, ratings, target)
        }
      case None => Future {
        new LearningResponse(false, "Can't find team with id: " + id)
      }
    }
  }

}

object LearningEngine {

  val BATCH = "bt"
  val REALTIME = "rt"

//  val PARAMS = new PriorParameters
//
//  val ALGORITHM = EMWithBP(PARAMS.probabilities: _*)
//
//  def startAlgorithm(): Unit = {
//    if (!ALGORITHM.isActive) ALGORITHM.start()
//  }
//
//  def stopAlgorithm(): Unit = {
//    if (ALGORITHM.isActive) ALGORITHM.kill()
//  }
}
