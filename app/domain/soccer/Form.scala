package domain.soccer

import java.time.LocalDateTime

import play.api.libs.json.Json

case class Form(
          teamId: String,
          numberOfWins: Int,
          numberOfLoses: Int,
          numberOfDraws: Int,
          dateCreated: LocalDateTime = LocalDateTime.now
          ) {}

object Form {
  implicit val formFormat = Json.format[Form]
}
