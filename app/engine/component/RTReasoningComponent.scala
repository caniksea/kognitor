package engine.component

import model.ReasoningResponse

object RTReasoningComponent {
  def reason(team: String): ReasoningResponse =
    if (team.equals("Manchester City")) ReasoningResponse(0.58)
    else ReasoningResponse(0.36)

}
