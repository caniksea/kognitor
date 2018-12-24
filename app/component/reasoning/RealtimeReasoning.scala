package component.reasoning

import domain.request.Previse

object RealtimeReasoning {
  def reason(team: String): Double =
    if (team.equals("Manchester City")) 0.58
    else 0.36

  def reason(previse: Previse) = null
}
