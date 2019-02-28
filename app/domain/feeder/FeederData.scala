package domain.feeder

import domain.soccer.{Fixture, Form}
import play.api.libs.json.Json

case class FeederData(
                     teamName: String,
                     rating: Double,
                     form: Form,
                     fixture: Fixture
                     )

object FeederData{
  implicit val feederDataFormat = Json.format[FeederData]
}
