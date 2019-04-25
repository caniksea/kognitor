package component.reasoning

import com.cra.figaro.algorithm.factored.VariableElimination
import component.{Model, PostParameters, ReasoningModel}
import domain.reasoning.{Previse, PreviseResult}
import domain.soccer.TeamProbability
import services.soccer.TeamProbabilityService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Reason {

  def doReason(prob: TeamProbability) = {
    println("prob:: ", prob)
    try {
      val learnedParams: PostParameters =
        new PostParameters(prob.winProbability, prob.goodRatingProbability, prob.badRatingProbability,
          prob.goodFormProbability, prob.badFormProbability, prob.goodHead2HeadProbability, prob.badHead2HeadProbability)
      println("learnedParams:: ", learnedParams)
      val model: Model = new ReasoningModel(learnedParams)
      println("Did you get here...")
      println("model:: ", model, model.hasGoodForm)
      val algorithm = VariableElimination(model.isWinner)
      algorithm.start()
      val isWinProbability = algorithm.probability(model.isWinner, true)
      println("Win probability: " + isWinProbability)
      algorithm.kill()
      Some(isWinProbability)
    } catch {
      case e: Exception => {
        println(e.getMessage)
        println(e.printStackTrace)
        None
      }
    }
  }

  def processProbabilities(probabilities: Option[TeamProbability]) = {
    probabilities match {
      case Some(value) => doReason(value)
      case None => None
    }
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
        val res = (batchValue * 0.25 + realtimeValue * 0.75)
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
      reasoning match {
        case Some(value) => PreviseResult(true, "Reasoning successful!", value)
        case None => PreviseResult(false, "An error occurred!")
      }
    }
  }

  def processRequest(request: Previse): Future[PreviseResult] = {
    val reasoningType = request.reasoningOption
    println(reasoningType)
    val service: TeamProbabilityService = reasoningType.toLowerCase match {
      case "bc" => TeamProbabilityService.batchViewImpl
      case "rt" => TeamProbabilityService.realtimeViewImpl
      case _ => null
    }

    if(service != null) processSingle(request, service)
    else processBoth(request)
  }

}
