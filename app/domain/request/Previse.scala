package domain.request

import play.api.libs.json.Json

case class Previse(team: String, model: String = "RT")

object Previse{
  implicit val queryRequestFormat = Json.format[Previse]
}
