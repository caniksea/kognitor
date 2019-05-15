package domain.soccer

import java.time.{LocalDate, LocalDateTime}

import domain.common.FixtureData
import play.api.libs.json.Json

case class Fixture(
                    homeTeamId: String,
                    awayTeamId: String,
                    homeTeamGoals: Int,
                    awayTeamGoals: Int,
                    override val dateCreated: LocalDate
                    ) extends FixtureData {}


object Fixture {
  implicit val head2HeadFormat = Json.format[Fixture]
}
