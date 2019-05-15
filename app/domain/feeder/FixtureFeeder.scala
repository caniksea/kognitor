package domain.feeder

import java.time.{LocalDate, LocalDateTime}

import domain.common.FixtureData
import play.api.libs.json.Json

case class FixtureFeeder(
                          teamName: String,
                          awayTeamName: String,
                          homeTeamGoals: Int,
                          awayTeamGoals: Int,
                          override val dateCreated: LocalDate
                        ) extends FixtureData {}

object FixtureFeeder {
  implicit val fixtureFeederFormat = Json.format[FixtureFeeder]
}
