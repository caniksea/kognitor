package component.reasoning

import component.{Model, PostParameters}
import domain.reasoning.Previse
import services.soccer.{Head2HeadService, TeamProbabilityService}

import scala.concurrent.ExecutionContext.Implicits.global

object BatchReasoning {
  def reason(team: String): Double =
    if (team.equals("Manchester City")) 0.78
    else 0.56

  def getTeamProbabilities(homeTeamId: String) = {
    val teamProbabilities = for {
      result <- TeamProbabilityService.batchViewImpl.getEntity(homeTeamId)
    } yield result
    teamProbabilities.map(result => result.getOrElse(null))
  }

  def reason(previse: Previse) = {
    val homeTeamId = previse.homeTeamId
    val awayTeamId = previse.awayTeamId
    val homeTeamH2H = for {
      results <- Head2HeadService.masterImpl.getHomeTeamMatches(homeTeamId)
    } yield results
    homeTeamH2H.map(result => result)

    val teamProbabilities = getTeamProbabilities(homeTeamId)
    teamProbabilities.map(probabilities => {
      val formProbability = probabilities.formProbability
      val ratingProbability = probabilities.ratingProbability
      val head2headHomeWinsProbability = probabilities.head2headHomeWinsProbability
      val postParameters: PostParameters = new PostParameters(formProbability, ratingProbability, head2headHomeWinsProbability)
    })

  }
}
