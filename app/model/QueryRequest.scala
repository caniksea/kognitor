package model

import play.api.libs.json.Json

case class QueryRequest(team: String, model: String = "RT")

object QueryRequest{
  implicit val queryRequestFormat = Json.format[QueryRequest]
}
