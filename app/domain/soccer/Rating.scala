package domain.soccer

import java.time.LocalDateTime

import play.api.libs.json.Json

class Rating(teamId: String, rating: Double, dateCreated: LocalDateTime = LocalDateTime.now) {}

object Rating {
  implicit val ratingFormat = Json.format[Rating]
}
