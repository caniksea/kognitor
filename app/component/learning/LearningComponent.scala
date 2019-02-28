package component.learning

import com.cra.figaro.algorithm.learning.EMWithBP
import component.soccer.TeamHelper
import component.{LearningModel, Model, PostParameters, PriorParameters}
import domain.learning.LearningResponse
import domain.soccer.{Form, Fixture, TeamProbability}
import services.soccer.{FormService, FixtureService, TeamProbabilityService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object LearningComponent {

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

    val models: Seq[Model] = learnHomeAdv(teamH2H)

    learnForm(models, teamForms)

  }

  def getTeamForms(teamId: String) =
    FormService.masterImpl.getTeamForms(teamId)

  def saveResult(teamProbabilities: TeamProbability) =
    TeamProbabilityService.batchViewImpl.saveEntity(teamProbabilities)

  /**
    * starting point. Should learn for all teams in the db
    * @param teamId
    * @return
    */
  def learn(teamId: String): Future[LearningResponse] = {
    val models = for {
      teamH2H <- getTeamMatches(teamId)
      teamForms <-getTeamForms(teamId)
    } yield{
      learnModels(teamH2H, teamForms)
    }
    models.map( model => {
      val result = learnMAP(new PriorParameters)
      val teamProbabilities = new TeamProbability(teamId, result.head2headHomeWinsProbability,
        result.ratingProbability, result.formProbability)
      // Save parameters before returning.
      saveResult(teamProbabilities)
      new LearningResponse(true, "Learning completed", teamProbabilities)
    })

  }


  def learnMAP(parameters: PriorParameters): PostParameters = {
    val algorithm = EMWithBP(parameters.probabilities._1: _*)
    algorithm.start()
    val head2headHomeWinProbability = parameters.head2headHomeWinsProbability.MAPValue
    val formProbability = parameters.formProbability.MAPValue
    val ratingProbability = parameters.probabilities._2.generateRandomness()
    algorithm.kill()
    new PostParameters(head2headHomeWinProbability, formProbability, ratingProbability)
  }

}
