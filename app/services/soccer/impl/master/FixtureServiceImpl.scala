package services.soccer.impl.master

import java.time.LocalDate

import domain.soccer.Fixture
import repositories.soccer.FixtureRepository
import services.soccer.FixtureService

import scala.concurrent.Future

class FixtureServiceImpl extends FixtureService {

  override def saveEntity(entity: Fixture): Future[Boolean] =
    FixtureRepository.masterImpl.saveEntity(entity)

  override def getEntities: Future[Seq[Fixture]] = FixtureRepository.masterImpl.getEntities

  override def getHomeTeamMatches(homeTeamId: String): Future[Seq[Fixture]] =
    FixtureRepository.masterImpl.getHomeTeamMatches(homeTeamId)

  override def getEntity(homeTeamId: String): Future[Option[Fixture]] =
    FixtureRepository.masterImpl.getEntity(homeTeamId)

  override def createTable: Future[Boolean] = FixtureRepository.masterImpl.createTable

  override def deleteEntity(entity: Fixture): Future[Boolean] = FixtureRepository.masterImpl.deleteEntity(entity)

  override def clear: Future[Boolean] = ???

  override def deleteByTeamId(teamId: String): Future[Boolean] = ???

  override def getTeamMatchForDate(teamId: String, date: LocalDate): Future[Seq[Fixture]] = ???
}
