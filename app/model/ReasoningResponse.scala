package model

import play.api.libs.json.Json

case class ReasoningResponse(score: Double)

object ReasoningResponse{
  implicit val reasoningResponseFormat = Json.format[ReasoningResponse]
}