package component.learning

import com.cra.figaro.algorithm.learning.EMWithBP
import component.soccer.TeamHelper
import component.{LearningModel, Model, PostParameters, PriorParameters}
import domain.learning.LearningResponse
import domain.soccer.{Fixture, Form, Team, TeamProbability}
import services.soccer.{FixtureService, FormService, RatingService, TeamProbabilityService, TeamService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object LearningComponent {

  val BATCH = "bt"
  val REALTIME = "rt"

  def getPriorParams = new PriorParameters

  /**
    * Iterate throught the team seq and learn on each to produce a future of sequence
    *
    * @param teams
    * @param target
    * @return
    */
  def processLearnForTeams(teams: Seq[Team], target: String): Future[Seq[LearningResponse]] = {
    println("Teams: " + teams)
    val s = for {
      team <- teams
    } yield {
      learn(team.teamId, target)
    }

    /**
      * s is a sequence of futures, i want a future of sequence
      */
    //    val s = teams.map(team => learn(team.teamId, target)) // this produces a sequence of futures
    Future.sequence(s) // this converts the sequence of futures into future of sequence
  }

  /**
    * Learns on a team given a target (Batch or Realtime)
    *
    * @param teamId
    * @return
    */
  def learn(teamId: String, target: String): Future[LearningResponse] = {
    for {
      team <- TeamService.masterImpl.getEntity(teamId)
      response <- processLearn(team, teamId, target)
    } yield {
      response
    }
  }

  /**
    * Method to learn on all teams given a target (Batch or Realtime)
    *
    * @param target
    * @return
    */
  def learnForAll(target: String): Future[Seq[LearningResponse]] = {
    println("Learning for all, target is: " + target)
    println("Getting teams...")
    for {
      teams <- TeamService.masterImpl.getEntities
      learnings <- processLearnForTeams(teams, target)
    } yield {
      learnings
    }
  }


  /**
    * Get label for good/bad form based on
    * GoodForm: (number of wins + number of draws/2) > (number of loses + number of draw/2)
    *
    * @param teamForms
    * @return
    */
  def getGoodnBadForms(teamForms: Seq[Form]) =
    (teamForms.filter(form => form.numberOfWins + form.numberOfDraws / 2 > form.numberOfLoses + form.numberOfDraws / 2),
      teamForms.filter(form => form.numberOfLoses + form.numberOfDraws / 2 > form.numberOfWins + form.numberOfDraws / 2))

  /**
    * Get win and lose matches
    *
    * @param teamH2H
    * @return
    */
  def getWinsnLoses(teamH2H: Seq[Fixture]) = {
    (teamH2H.filter(h2h => h2h.homeTeamGoals >= h2h.awayTeamGoals),
      teamH2H.filter(h2h => h2h.awayTeamGoals > h2h.homeTeamGoals))
  }

  def saveGlobal(teamProbabilities: TeamProbability) =
    for {
      batchSave <- TeamProbabilityService.batchViewImpl.saveEntity(teamProbabilities)
      rtSave <- TeamProbabilityService.realtimeViewImpl.saveEntity(teamProbabilities)
    } yield {
      if (batchSave) println("Saved in batch db")
      if (rtSave) println("Saved in real time db")
      batchSave && rtSave
    }

  def saveResult(teamProbabilities: TeamProbability, target: String) =
    if (target.equals(BATCH))
      saveGlobal(teamProbabilities)
    else
      for {
        rtSave <- TeamProbabilityService.realtimeViewImpl.saveEntity(teamProbabilities)
      } yield rtSave

  def cleanRT() = for {
    clearRating <- RatingService.pseudomasterImpl.clear
    clearFixture <- FixtureService.pseudomasterImpl.clear
    clearForm <- FormService.pseudomasterImpl.clear
  } yield {
    if (clearFixture) println("RT Fixtures cleared!")
    if (clearForm) println("RT Forms cleared!")
    if (clearRating) println("RT Ratings cleared!")
    clearFixture && clearForm && clearRating
  }

  def getTeamMatches(teamId: String, target: String) =
    if (target.equals(BATCH))
      FixtureService.masterImpl.getHomeTeamMatches(teamId)
    else
      FixtureService.pseudomasterImpl.getHomeTeamMatches(teamId)

  def getTeamForms(teamId: String, target: String) =
    if (target.equals(BATCH))
      FormService.masterImpl.getTeamForms(teamId)
    else
      FormService.pseudomasterImpl.getTeamForms(teamId)

  def getFixtureLabels(teamH2H: Seq[Fixture]) = {
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

  def getFormLabels(teamForms: Seq[Form]) = {
    val oneThird = teamForms.size / 3
    val formLabelMaxLength = if (oneThird == 0) teamForms.size else oneThird

    val (goodForms, badForms) = getGoodnBadForms(teamForms)

    val oneThirdGoodForms = goodForms.size / 3
    val goodFormSize = if (oneThirdGoodForms == 0) goodForms.size else oneThirdGoodForms
    val goodFormLabelSize = if (goodFormSize < formLabelMaxLength) formLabelMaxLength else goodFormSize

    val oneThirdBadForms = badForms.size / 3
    val badFormSize = if (oneThirdBadForms == 0) badForms.size else oneThirdBadForms
    val badFormLabelSize = if (badFormSize < formLabelMaxLength) formLabelMaxLength else badFormSize

    val labeledGoodForms = goodForms.take(goodFormLabelSize)
    val labeledBadForms = badForms.take(badFormLabelSize)

    for {
      form <- teamForms
    } yield {
      if (labeledGoodForms.contains(form)) Some(true)
      else if (labeledBadForms.contains(form)) Some(false)
      else None
    }
  }

  def learnOnLabels(fixtureLabel: Option[Boolean], formLabel: Option[Boolean], params: PriorParameters) = {
    val model: LearningModel = new LearningModel(params)
    val teamHelper = new TeamHelper
    teamHelper.observeEvidence(model, fixtureLabel, formLabel)
    println("model for form::: ", model)
    println()
    model
  }

  def learnModels(teamH2H: Seq[Fixture], teamForms: Seq[Form], target: String, params: PriorParameters) = {

    val teamId = teamForms.head.teamId

    println("Team Fixtures for team with id: " + teamId + " = " + teamH2H)
    println()
    println("Team Forms for team with id: " + teamId + " = " + teamForms)
    println()

    val fixtureLabels = getFixtureLabels(teamH2H)
    val formLabels = getFormLabels(teamForms)

    for {
      fixtureLabel <- fixtureLabels
      formLabel <- formLabels
    } yield {
      learnOnLabels(fixtureLabel, formLabel, params)
    }

  }

  /**
    * Performs learning on team
    *
    * @param team
    * @param id
    * @param target
    * @return
    */
  def processLearn(team: Option[Team], id: String, target: String) = {
    team match {
      case Some(t) => {
        println("Learning for team: " + t.teamName + "..." + " | Team ID: " + t.teamId)
        println()
        val params = getPriorParams
        for {
          teamForms <- getTeamForms(t.teamId, target)
          teamH2H <- getTeamMatches(t.teamId, target)
        } yield {
          val models = learnModels(teamH2H, teamForms, target, params)
          val result = learnMAP(params)
          println(t.teamId, result.winProbability, result.goodRatingProbability, result.badRatingProbability,
            result.goodFormProbability, result.badFormProbability, result.goodHead2HeadProbability, result.badHead2HeadProbability)
          println()
          val teamProbabilities =
            TeamProbability(t.teamId, result.winProbability, result.goodRatingProbability, result.badRatingProbability,
              result.goodFormProbability, result.badFormProbability, result.goodHead2HeadProbability, result.badHead2HeadProbability)
          saveResult(teamProbabilities, target)
          if (target.equals(BATCH)) cleanRT()
          new LearningResponse(true, "Learning completed for team with id: " + t.teamId, teamProbabilities)
        }
      }
      case None => Future {
        new LearningResponse(false, "Can't find team with id: " + id)
      }
    }
  }


  /**
    * Learning using figaro
    *
    * @param parameters
    * @return
    */
  def learnMAP(parameters: PriorParameters): PostParameters = {
    val algorithm = EMWithBP(parameters.probabilities: _*)
    algorithm.start()
    val winProbability = parameters.winProbability.MAPValue
    val goodRatingProbability = parameters.goodRatingProbability.MAPValue
    val badRatingProbability = parameters.badRatingProbability.MAPValue
    val goodFormProbability = parameters.goodFormProbability.MAPValue
    val badFormProbability = parameters.badFormProbability.MAPValue
    val goodHead2HeadProbability = parameters.goodHead2HeadProbability.MAPValue
    val badHead2HeadProbability = parameters.badHead2HeadProbability.MAPValue
    algorithm.kill()
    new PostParameters(winProbability, goodRatingProbability, badRatingProbability, goodFormProbability, badFormProbability, goodHead2HeadProbability, badHead2HeadProbability)
  }

}
