package component.reasoning

import com.cra.figaro.algorithm.factored.VariableElimination
import component.{Model, PostParameters, ReasoningModel}
import domain.reasoning.Previse
import domain.soccer.{Head2Head, TeamProbability}
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

  def batchReason(prob: TeamProbability) = {
    val learnedParams: PostParameters =
      new PostParameters(prob.head2headHomeWinsProbability, prob.formProbability, prob.ratingProbability)
    val model = new ReasoningModel(learnedParams)
//    val algorithm = VariableElimination(model.isWinner)
//    algorithm.start()
//    val isWinProbability = algorithm.probability(model.isWinner)
//    println("Win probability: " + isWinProbability)
//    algorithm.kill()
//    isWinProbability
  }

  def process(probabilities: Option[TeamProbability]) = {
    val prob: TeamProbability = probabilities.getOrElse(null)
    if(prob != null) {
      batchReason(prob)
    }
  }

  def reason(previse: Previse) = {
    val homeTeamH2H = for {
      probabilities <- TeamProbabilityService.batchViewImpl.getEntity(previse.homeTeamId)
//      results <- Head2HeadService.masterImpl.getHomeTeamMatches(previse.homeTeamId)
    } yield {
      process(probabilities)
    }
//    homeTeamH2H.map(result => result)
//
//    val teamProbabilities = getTeamProbabilities(homeTeamId)
//    teamProbabilities.map(probabilities => {
//      val formProbability = probabilities.formProbability
//      val ratingProbability = probabilities.ratingProbability
//      val head2headHomeWinsProbability = probabilities.head2headHomeWinsProbability
//      val postParameters: PostParameters = new PostParameters(formProbability, ratingProbability, head2headHomeWinsProbability)
//    })

  }
}
