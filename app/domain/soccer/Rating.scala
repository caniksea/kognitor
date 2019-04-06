package domain.soccer

import domain.common.RatingData
import play.api.libs.json.Json

case class Rating(teamId: String,
                  rating: Double,
                 ) extends RatingData {}

object Rating {
  implicit val ratingFormat = Json.format[Rating]
}
