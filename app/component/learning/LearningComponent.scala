package component.learning

import com.cra.figaro.algorithm.learning.EMWithBP
import component.soccer.TeamHelper
import component.{LearningModel, PostParameters, PriorParameters}
import domain.learning.LearningResponse
import domain.soccer.{Form, Head2Head, TeamProbability}
import services.soccer.{FormService, Head2HeadService, TeamProbabilityService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object LearningComponent {

  def getPriorParams = new PriorParameters

  def getTeamMatches(teamId: String) =
    Head2HeadService.masterImpl.getHomeTeamMatches(teamId)


  def getGoodnBadForms(teamForms: Seq[Form]) =
    (teamForms.filter(form => form.numberOfWins > form.numberOfLoses),
      teamForms.filter(form => form.numberOfLoses > form.numberOfWins))

  def getWinsnLoses(teamH2H: Seq[Head2Head]) = {
    (teamH2H.filter(h2h => h2h.homeTeamGoals >= h2h.awayTeamGoals),
      teamH2H.filter(h2h => h2h.awayTeamGoals > h2h.homeTeamGoals))
  }

  def learnHomeAdv(priorParams: PriorParameters, teamH2H: Seq[Head2Head]) = {
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
      val model = new LearningModel(priorParams)
      teamHelper.observeHomeGroundAdv(model, win)
      model
    })
  }

  def learnForm(priorParams: PriorParameters, teamForms: Seq[Form]) = {
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
      val model = new LearningModel(priorParams)
      teamHelper.observeForm(model, form)
      model
    })
  }

  def learnModels(teamH2H: Seq[Head2Head], teamForms: Seq[Form]) = {
    val priorParams = getPriorParams

    learnHomeAdv(priorParams, teamH2H)

    learnForm(priorParams, teamForms)

  }

  def getTeamForms(teamId: String) =
    FormService.masterImpl.getTeamForms(teamId)

  def saveResult(teamProbabilities: TeamProbability) =
    TeamProbabilityService.batchViewImpl.saveEntity(teamProbabilities)

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
    val head2headHomeWinProbability = parameters.head2headHomeWinsProbability.MAPValue
    val formProbability = parameters.formProbability.MAPValue
    val ratingProbability = parameters.probabilities._2.generateRandomness()
    algorithm.kill()
    new PostParameters(head2headHomeWinProbability, formProbability, ratingProbability)
  }

}
