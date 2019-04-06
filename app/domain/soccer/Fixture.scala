package domain.soccer

import java.time.LocalDateTime

import domain.common.FixtureData
import play.api.libs.json.Json

case class Fixture(
                      homeTeamId: String,
                      awayTeamId: String,
                      homeTeamGoals: Int,
                      awayTeamGoals: Int,
                      dateOfCompetition: LocalDateTime
                    ) extends FixtureData {}


object Fixture {
  implicit val head2HeadFormat = Json.format[Fixture]
}
