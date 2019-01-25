package domain.reasoning

import play.api.libs.json.Json

case class PreviseResult (isSuccessful: Boolean, description: String, result: Double)

object PreviseResult{
  implicit val previseResultFormat = Json.format[PreviseResult]
}