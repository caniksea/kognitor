package domain.soccer

import play.api.libs.json.Json

@Deprecated
class RatingSummary(
                   teamId: String,
                   winningStreaks: Int,
                   currentRating: Double
                   ) {}

object RatingSummary {
  implicit val ratingSummaryFormat = Json.format[RatingSummary]
}
