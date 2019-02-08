package domain.reasoning

import play.api.libs.json.Json

case class Previse(homeTeamId: String, awayTeamId: String, reasoningOption: String = "both")

object Previse{
  implicit val queryRequestFormat = Json.format[Previse]
}
