package domain.soccer

import play.api.libs.json.Json

case class Team(teamId: String, teamName: String) {}

object Team {
  implicit val teamFormat = Json.format[Team]
}
