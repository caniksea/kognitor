package component.request

import component.reasoning.{Batch, RealTime}
import domain.request.Previse
import domain.response.PreviseResult

object PreviseComponent {
  def previse(request: Previse): PreviseResult = {
    val team = request.team
    val model = request.model
    val response = if (model.equals("RT")) RealTime.reason(team)
    else Batch.reason(team)
    PreviseResult(true, "Reasoning was successful", response)
  }
}
