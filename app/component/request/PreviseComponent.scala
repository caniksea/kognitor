package component.request

import component.reasoning.{BatchReasoning, CombinedReasoning, RealtimeReasoning}
import domain.reasoning.{Previse, PreviseResult}

import scala.concurrent.Future

object PreviseComponent {
  def previse(request: Previse): Future[PreviseResult] = {
    val reasoningOption = request.reasoningOption
    val response = reasoningOption.toLowerCase match {
      case "bc" => BatchReasoning.reason(request)
      case "rc" => RealtimeReasoning.reason(request)
      case "both" => CombinedReasoning.reason(request)
      case _ => "Unknown reasoning type"
    }
//    val response = if (model.equals("RT")) RealTime.reason(team)
//    else Batch.reason(team)
//    PreviseResult(true, "Reasoning was successful", response)
    null
  }
}
