package component.reasoning

import com.cra.figaro.algorithm.factored.VariableElimination
import component.{PostParameters, ReasoningModel}
import domain.reasoning.{Previse, PreviseResult}
import domain.soccer.TeamProbability
import services.soccer.TeamProbabilityService

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

object Reason {

  def doReason(prob: TeamProbability) = {
    try {
      val learnedParams: PostParameters =
        new PostParameters(prob.head2headHomeWinsProbability, prob.formProbability, prob.ratingProbability)
      val model = new ReasoningModel(learnedParams)
      val algorithm = VariableElimination(model.isWinner)
      algorithm.start()
      val isWinProbability = algorithm.probability(model.isWinner.value, true)
      println("Win probability: " + isWinProbability)
      algorithm.kill()
      Some(isWinProbability)
    } catch {
      case e: Exception => {
        println(e.getMessage)
        None
      }
    }
  }

  def processProbabilities(probabilities: Option[TeamProbability]) = {
    val prob: TeamProbability = probabilities.getOrElse(null)
    if(prob != null) {
      doReason(prob)
    } else None
  }

  def runReasoning(request: Previse, service: TeamProbabilityService) = {
    for {
      probabilities <- service.getEntity(request.homeTeamId)
    } yield {
      processProbabilities(probabilities)
    }
  }

  def processBoth(request: Previse): Future[PreviseResult] = {
    for{
      batchReasoning <- runReasoning(request, TeamProbabilityService.batchViewImpl)
      realtimeReasoning <- runReasoning(request, TeamProbabilityService.realtimeViewImpl)
    } yield {
      val batchValue: Double = batchReasoning.getOrElse(-1)
      val realtimeValue: Double = realtimeReasoning.getOrElse(-1)
      if(batchValue != -1 && realtimeValue != -1) {
        val res = (batchValue * 0.2 + realtimeValue * 0.8)
        PreviseResult(true, "Reasoning successful!", res)
      } else {
        PreviseResult(false, "An error occurred!", 0)
      }
    }
  }

  def processSingle(request: Previse, service: TeamProbabilityService): Future[PreviseResult] = {
    for {
      reasoning <- runReasoning(request, service)
    } yield {
      val result: Double = reasoning.getOrElse(-1)
      if (result != -1) PreviseResult(true, "Reasoning successful!", result)
      else PreviseResult(false, "An error occurred!", result)
    }
  }

  def processRequest(request: Previse): Future[PreviseResult] = {
    val reasoningType = request.reasoningOption
    val service: TeamProbabilityService = reasoningType.toLowerCase match {
      case "bc" => TeamProbabilityService.batchViewImpl
      case "rt" => TeamProbabilityService.realtimeViewImpl
      case _ => null
    }

    if(service != null) processSingle(request, service)
    else processBoth(request)
  }

}
