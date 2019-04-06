package domain.feeder

import domain.common.RatingData
import play.api.libs.json.Json

case class RatingFeeder(
                       teamName: String,
                       rating: Double
                       ) extends RatingData {}

object RatingFeeder {
  implicit val ratingFeederFormat = Json.format[RatingFeeder]
}
