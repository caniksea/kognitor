package domain.learning

import domain.soccer.TeamProbability
import play.api.libs.json.Json

case class LearningResponse(
                             isSuccessful: Boolean,
                             description: String,
                             result: TeamProbability
                           )

object LearningResponse {
  implicit val learningResponseFormat = Json.format[LearningResponse]
}
