package component.reasoning

import domain.request.Previse

object BatchReasoning {
  def reason(team: String): Double =
    if (team.equals("Manchester City")) 0.78
    else 0.56

  def reason(previse: Previse) = null
}
