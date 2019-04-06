package domain.feeder

import java.time.LocalDateTime

import play.api.libs.json.Json

case class FeederData(
                     ratingFeeders: Seq[RatingFeeder],
                     formFeeders: Seq[FormFeeder],
                     fixtureFeeder: Seq[FixtureFeeder],
                     dateCreated: LocalDateTime = LocalDateTime.now
                     )

object FeederData{
  implicit val feederDataFormat = Json.format[FeederData]
}
