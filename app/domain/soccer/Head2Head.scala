package domain.soccer

import java.time.LocalDateTime

import play.api.libs.json.Json

case class Head2Head(
                      homeTeamId: String,
                      awayTeamId: String,
                      homeTeamGoals: Int,
                      awayTeamGoals: Int,
                      dateOfCompetition: LocalDateTime,
                      dateCreated: LocalDateTime = LocalDateTime.now
                    ) {}

object Head2Head {
  implicit val head2HeadFormat = Json.format[Head2Head]
}
