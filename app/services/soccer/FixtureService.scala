package services.soccer

import java.time.LocalDate

import domain.soccer.Fixture
import services.CRUDService
import services.soccer.impl.{master, pseudomaster}

import scala.concurrent.Future

trait FixtureService extends CRUDService[Fixture] {

  def getTeamMatchForDate(teamId: String, date: LocalDate = LocalDate.now): Future[Seq[Fixture]]
  def getHomeTeamMatches(homeTeamId: String): Future[Seq[Fixture]]
  def clear: Future[Boolean]
  def deleteByTeamId(teamId: String): Future[Boolean]
}

object FixtureService {
  def masterImpl: FixtureService = new master.FixtureServiceImpl()
  def pseudomasterImpl: FixtureService = new pseudomaster.FixtureServiceImpl()
}
