package domain.soccer

import java.time.LocalDate

import domain.common.RatingData
import play.api.libs.json.Json

case class Rating(teamId: String,
                  rating: Double,
                  override val dateCreated: LocalDate
                 ) extends RatingData {}

object Rating {
  implicit val ratingFormat = Json.format[Rating]
}
