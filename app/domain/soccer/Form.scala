package domain.soccer

import java.time.{LocalDate, LocalDateTime}

import domain.common.FormData
import play.api.libs.json.Json

case class Form(
                 teamId: String,
                 numberOfWins: Int,
                 numberOfLoses: Int,
                 numberOfDraws: Int,
                 override val sourceDate: LocalDate,
                 dateCreated: LocalDateTime = LocalDateTime.now
          ) extends FormData {}

object Form {
  implicit val formFormat = Json.format[Form]
}
