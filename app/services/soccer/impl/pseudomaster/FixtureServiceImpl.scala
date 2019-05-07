package services.soccer.impl.pseudomaster

import domain.soccer.Fixture
import repositories.soccer.FixtureRepository
import services.soccer.FixtureService

import scala.concurrent.Future

class FixtureServiceImpl extends FixtureService {
  override def saveEntity(entity: Fixture): Future[Boolean] =
    FixtureRepository.pseudomasterImpl.saveEntity(entity)

  override def getEntities: Future[Seq[Fixture]] = FixtureRepository.pseudomasterImpl.getEntities

  override def getHomeTeamMatches(homeTeamId: String): Future[Seq[Fixture]] =
    FixtureRepository.pseudomasterImpl.getHomeTeamMatches(homeTeamId)

  override def getEntity(homeTeamId: String): Future[Option[Fixture]] =
    FixtureRepository.pseudomasterImpl.getEntity(homeTeamId)

  override def createTable: Future[Boolean] = FixtureRepository.pseudomasterImpl.createTable

  override def deleteEntity(entity: Fixture): Future[Boolean] =
    FixtureRepository.pseudomasterImpl.deleteEntity(entity)

  override def clear: Future[Boolean] = FixtureRepository.pseudomasterImpl.clear
}
