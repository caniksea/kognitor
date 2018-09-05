package domain.response

import play.api.libs.json.Json

case class PreviseResult (isSuccess: Boolean, description: String, result: Double)

object PreviseResult{
  implicit val previseResultFormat = Json.format[PreviseResult]
}