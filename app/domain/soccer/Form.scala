package domain.soccer

import domain.common.FormData
import play.api.libs.json.Json

case class Form(
          teamId: String,
          numberOfWins: Int,
          numberOfLoses: Int,
          numberOfDraws: Int,
          ) extends FormData {}

object Form {
  implicit val formFormat = Json.format[Form]
}
