package domain.feeder

import domain.common.FormData
import play.api.libs.json.Json

case class FormFeeder(
                       teamName: String,
                       numberOfWins: Int,
                       numberOfLoses: Int,
                       numberOfDraws: Int
                     ) extends FormData {}

object FormFeeder {
  implicit val formFeederFormat = Json.format[FormFeeder]
}
