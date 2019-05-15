package domain.feeder

import java.time.LocalDate

import domain.common.FormData
import play.api.libs.json.Json

case class FormFeeder(
                       teamName: String,
                       numberOfWins: Int,
                       numberOfLoses: Int,
                       numberOfDraws: Int,
                       override val dateCreated: LocalDate
                     ) extends FormData {}

object FormFeeder {
  implicit val formFeederFormat = Json.format[FormFeeder]
}
