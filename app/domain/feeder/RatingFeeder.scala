package domain.feeder

import java.time.LocalDate

import domain.common.RatingData
import play.api.libs.json.Json

case class RatingFeeder(
                         teamName: String,
                         rating: Double,
                         override val dateCreated: LocalDate
                       ) extends RatingData {}

object RatingFeeder {
  implicit val ratingFeederFormat = Json.format[RatingFeeder]
}
