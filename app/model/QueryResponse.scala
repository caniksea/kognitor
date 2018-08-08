package model

import play.api.libs.json.Json

case class QueryResponse(isSuccess: Boolean, description: String, result: ReasoningResponse)

object QueryResponse{
  implicit val queryResponseFormat = Json.format[QueryResponse]
}
