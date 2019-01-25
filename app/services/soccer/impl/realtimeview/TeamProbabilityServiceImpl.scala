package services.soccer.impl.realtimeview

import domain.soccer.TeamProbability
import repositories.soccer.TeamProbabilityRepository
import services.soccer.TeamProbabilityService

import scala.concurrent.Future

class TeamProbabilityServiceImpl extends TeamProbabilityService {
  override def saveEntity(entity: TeamProbability): Future[Boolean] =
    TeamProbabilityRepository.realtimeViewImpl.saveEntity(entity)

  override def getEntities: Future[Seq[TeamProbability]] =
    TeamProbabilityRepository.realtimeViewImpl.getEntities

  override def getEntity(teamId: String): Future[Option[TeamProbability]] =
    TeamProbabilityRepository.realtimeViewImpl.getEntity(teamId)

  override def createTable: Future[Boolean] = TeamProbabilityRepository.realtimeViewImpl.createTable

  override def deleteEntity(entity: TeamProbability): Future[Boolean] =
    TeamProbabilityRepository.realtimeViewImpl.deleteEntity(entity)
}
