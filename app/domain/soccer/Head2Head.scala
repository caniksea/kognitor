package domain.soccer

import java.time.LocalDateTime

import play.api.libs.json.Json

class Head2Head(homeTeamId: String,
                awayTeamId: String,
                homeTeamWin: String,
                dateOfCompetition: LocalDateTime,
                dateCreated: LocalDateTime = LocalDateTime.now
               ) {}

object Head2Head{
  implicit val head2HeadFormat = Json.format[Head2Head]
}
