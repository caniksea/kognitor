package domain.feeder

import java.time.LocalDateTime

import domain.common.FixtureData
import play.api.libs.json.Json

case class FixtureFeeder(
                        teamName: String,
                          awayTeamName: String,
                          homeTeamGoals: Int,
                          awayTeamGoals: Int,
                          dateOfCompetition: LocalDateTime,
                        ) extends FixtureData { }

object FixtureFeeder {
  implicit val fixtureFeederFormat = Json.format[FixtureFeeder]
}
