package component.learning

import com.cra.figaro.algorithm.learning.EMWithBP
import component.soccer.TeamHelper
import component.{LearningModel, Model, PostParameters, PriorParameters}
import domain.learning.LearningResponse
import domain.soccer.{Fixture, Form, Team, TeamProbability}
import services.soccer.{FixtureService, FormService, TeamProbabilityService, TeamService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object LearningComponent {

  def processLearnForTeams(teams: Seq[Team]): Future[Seq[LearningResponse]] = {
    println(teams)
    val s = teams.map(team => learn(team.teamId)) // this produces a sequence of futures
    Future.sequence(s) // this converts the sequence of futures into future of sequence
  }


  def learnForAll: Future[Seq[LearningResponse]] =
    for {
      teams <- TeamService.masterImpl.getEntities
      learnings <- processLearnForTeams(teams)
    } yield {
      println(learnings)
      learnings
    }


  def getPriorParams = new PriorParameters

  def getTeamMatches(teamId: String) =
    FixtureService.masterImpl.getHomeTeamMatches(teamId)


  /**
    * Get label for good/bad form based on
    * GoodForm: (number of wins + number of draws/2) > (number of loses + number of draw/2)
    * @param teamForms
    * @return
    */
  def getGoodnBadForms(teamForms: Seq[Form]) =
    (teamForms.filter(form => form.numberOfWins + form.numberOfDraws / 2 > form.numberOfLoses + form.numberOfDraws / 2),
      teamForms.filter(form => form.numberOfLoses + form.numberOfDraws / 2 > form.numberOfWins + form.numberOfDraws / 2))

  /**
    * Get win and lose matches
    * @param teamH2H
    * @return
    */
  def getWinsnLoses(teamH2H: Seq[Fixture]) = {
    (teamH2H.filter(h2h => h2h.homeTeamGoals >= h2h.awayTeamGoals),
      teamH2H.filter(h2h => h2h.awayTeamGoals > h2h.homeTeamGoals))
  }

  def learnHomeAdv(teamH2H: Seq[Fixture]) = {
    val h2hMaxLabelLength = teamH2H.size / 3
    val (wins, loses) = getWinsnLoses(teamH2H)
    val winLabelSize = if (wins.size / 3 < h2hMaxLabelLength) h2hMaxLabelLength else wins.size / 3
    val losesLabelSize = if (loses.size / 3 < h2hMaxLabelLength) h2hMaxLabelLength else loses.size / 3

    //TODO: remove later
    println("h2hMaxLabelLength", h2hMaxLabelLength)
    println("wins.size", wins.size)
    println("winLabelSize", winLabelSize)
    println("loses.size", loses.size)
    println("losesLabelSize", losesLabelSize)
    println("wins.size", wins.size)

    val labeledWins = wins.take(winLabelSize)
    val labeledLoses = loses.take(losesLabelSize)

    val labeled = teamH2H.map(h2h => {
      if(labeledWins.contains(h2h)) Some(true)
      else if(labeledLoses.contains(h2h)) Some(false)
      else None
    })

    labeled.map( win => {
      val teamHelper = new TeamHelper
      val model: Model = new LearningModel(getPriorParams)
      teamHelper.observeHomeGroundAdv(model, win)
      model
    })
  }

  def learnForm(model: Seq[Model], teamForms: Seq[Form]) = {
    val formMaxLabelLength = teamForms.size / 3
    val (goodForms, badForms) = getGoodnBadForms(teamForms)
    val goodFormLabelSize = if (goodForms.size / 3 < formMaxLabelLength) formMaxLabelLength else goodForms.size / 3
    val badFormLabelSize = if (badForms.size / 3 < formMaxLabelLength) formMaxLabelLength else badForms.size / 3

    val labeledGoodForms = goodForms.take(goodFormLabelSize)
    val labeledBadForms = badForms.take(badFormLabelSize)

    val labeled = teamForms.map(form => {
      if(labeledGoodForms.contains(form)) Some(true)
      else if (labeledBadForms.contains(form)) Some(false)
      else None
    })

    labeled.map( form => {
      val teamHelper = new TeamHelper
      val model: Model = new LearningModel(getPriorParams)
      teamHelper.observeForm(model, form)
      model
    })
  }

  def learnModels(teamH2H: Seq[Fixture], teamForms: Seq[Form]) = {

    println("Head2Heads: ", teamH2H)
    println("Forms: ", teamForms)

    val models: Seq[Model] = learnHomeAdv(teamH2H)

    learnForm(models, teamForms)

  }

  def getTeamForms(teamId: String) =
    FormService.masterImpl.getTeamForms(teamId)

  def saveResult(teamProbabilities: TeamProbability) =
    for {
      batchSave <- TeamProbabilityService.batchViewImpl.saveEntity(teamProbabilities)
      rtSave <- TeamProbabilityService.realtimeViewImpl.saveEntity(teamProbabilities)
    } yield {
      if (batchSave) println("Saved in batch db")
      if (rtSave) println("Saved in real time db")
      batchSave && rtSave
    }

  def findTeam(teamId: String) = {
    for {
      team <- TeamService.masterImpl.getEntity(teamId)
    } yield {
      team
    }
  }

  def processLearn(team: Option[Team], id: String) = {
    team match {
      case Some(t) => {
        val models = for {
          teamH2H <- getTeamMatches(t.teamId)
          teamForms <-getTeamForms(t.teamId)
        } yield{
          learnModels(teamH2H, teamForms)
        }
        models.map( model => {
          val result = learnMAP(new PriorParameters)
          val teamProbabilities =
            TeamProbability(t.teamId, result.winProbability, result.goodRatingProbability, result.badRatingProbability,
              result.goodFormProbability, result.badFormProbability, result.goodHead2HeadProbability, result.badHead2HeadProbability)
          // Save parameters before returning.
          saveResult(teamProbabilities)
          new LearningResponse(true, "Learning completed for team with id: " + t.teamId, teamProbabilities)
        })
      }
      case None => Future {new LearningResponse(false, "Can't find team with id: " + id)}
    }
  }

  /**
    * starting point. Should learn for all teams in the db
    *
    * @param teamId
    * @return
    */
  def learn(teamId: String): Future[LearningResponse] = {
    val team = findTeam(teamId)
    for {
      team <- findTeam(teamId)
      response <- processLearn(team, teamId)
    } yield {
      response
    }
  }


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
