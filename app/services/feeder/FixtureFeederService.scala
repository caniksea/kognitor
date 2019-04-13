package services.feeder

import java.time.LocalDate

import domain.feeder.FixtureFeeder
import services.CRUDService
import services.feeder.impl.FixtureFeederServiceImpl

import scala.concurrent.Future

trait FixtureFeederService extends CRUDService[FixtureFeeder] {
  def getTeamFixture(teamName: String, date: LocalDate): Future[Option[FixtureFeeder]]
  def getTeamsFixture(teamList: List[String], date: LocalDate): Future[Seq[FixtureFeeder]]
}

object FixtureFeederService {
  def apply: FixtureFeederService = new FixtureFeederServiceImpl()
}
