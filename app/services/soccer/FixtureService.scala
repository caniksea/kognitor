package services.soccer

import domain.soccer.Fixture
import services.CRUDService
import services.soccer.impl.{master, pseudomaster}

import scala.concurrent.Future

trait FixtureService extends CRUDService[Fixture] {

  def getHomeTeamMatches(homeTeamId: String): Future[Seq[Fixture]]
  def clear: Future[Boolean]
}

object FixtureService {
  def masterImpl: FixtureService = new master.FixtureServiceImpl()
  def pseudomasterImpl: FixtureService = new pseudomaster.FixtureServiceImpl()
}
