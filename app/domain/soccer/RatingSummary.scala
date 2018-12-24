package domain.soccer

import play.api.libs.json.Json

class RatingSummary(
                   teamId: String,
                   winningStreaks: Int,
                   currentRating: Double
                   ) {}

object RatingSummary {
  implicit val ratingSummaryFormat = Json.format[RatingSummary]
}
