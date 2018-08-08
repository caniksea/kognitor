package engine.component

import model.ReasoningResponse

object BatchReasoningComponent {
  def reason(team: String): ReasoningResponse =
    if (team.equals("Manchester City")) ReasoningResponse(0.78)
    else ReasoningResponse(0.56)

}
