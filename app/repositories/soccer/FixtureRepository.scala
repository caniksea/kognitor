package repositories.soccer

import java.time.LocalDate

import domain.soccer.Fixture
import repositories.CRUDRepository
import repositories.soccer.impl.cassandra.{master, pseudomaster}

import scala.concurrent.Future

trait FixtureRepository extends CRUDRepository[Fixture] {

  def getTeamMatchForDate(teamId: String, date: LocalDate): Future[Seq[Fixture]]

  def getHomeTeamMatches(homeTeamId: String): Future[Seq[Fixture]]

  def clear: Future[Boolean]

  def deleteByTeamId(teamId: String): Future[Boolean]

}

object FixtureRepository {
  def masterImpl: FixtureRepository = new master.FixtureRepositoryImpl()

  def pseudomasterImpl: FixtureRepository = new pseudomaster.FixtureRepositoryImpl()
}
