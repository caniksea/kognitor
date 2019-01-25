package domain.learning

import play.api.libs.json.Json

case class LearningRequest(teamId: String)

object LearningRequest {
  implicit val learningRequestFormat = Json.format[LearningRequest]
}
