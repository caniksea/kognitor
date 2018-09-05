package component.reasoning

object RealTime {
  def reason(team: String): Double =
    if (team.equals("Manchester City")) 0.58
    else 0.36
}
