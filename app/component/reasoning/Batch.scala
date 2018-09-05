package component.reasoning

object Batch {
  def reason(team: String): Double =
    if (team.equals("Manchester City")) 0.78
    else 0.56
}
