package repositories.feeder

import java.time.LocalDate

import domain.feeder.FixtureFeeder
import repositories.CRUDRepository
import repositories.feeder.impl.cassandra.FixtureFeederRepositoryImpl

import scala.concurrent.Future

trait FixtureFeederRepository extends CRUDRepository[FixtureFeeder] {
  def getTeamFixture(teamName: String, date: LocalDate): Future[Option[FixtureFeeder]]
}

object FixtureFeederRepository {
  def apply: FixtureFeederRepository = new FixtureFeederRepositoryImpl()
}
