package services.feeder.impl

import java.time.LocalDate

import domain.feeder.FixtureFeeder
import repositories.feeder.FixtureFeederRepository
import services.feeder.FixtureFeederService

import scala.concurrent.Future

class FixtureFeederServiceImpl extends FixtureFeederService {
  override def getTeamFixture(teamName: String, date: LocalDate): Future[Option[FixtureFeeder]] =
    FixtureFeederRepository.apply.getTeamFixture(teamName, date)

  override def saveEntity(entity: FixtureFeeder): Future[Boolean] =
    FixtureFeederRepository.apply.saveEntity(entity)

  override def getEntities: Future[Seq[FixtureFeeder]] = ???

  override def getEntity(id: String): Future[Option[FixtureFeeder]] = ???

  override def deleteEntity(entity: FixtureFeeder): Future[Boolean] = ???

  override def createTable: Future[Boolean] = FixtureFeederRepository.apply.createTable

  override def getTeamsFixture(teamList: List[String], date: LocalDate): Future[Seq[FixtureFeeder]] =
    FixtureFeederRepository.apply.getTeamsFixture(teamList, date)
}
