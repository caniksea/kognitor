package services.soccer.impl.batchview

import domain.soccer.TeamProbability
import repositories.soccer.TeamProbabilityRepository
import services.soccer.TeamProbabilityService

import scala.concurrent.Future

class TeamProbabilityServiceImpl extends TeamProbabilityService {
  override def saveEntity(entity: TeamProbability): Future[Boolean] =
    TeamProbabilityRepository.batchViewImpl.saveEntity(entity)

  override def getEntities: Future[Seq[TeamProbability]] =
    TeamProbabilityRepository.batchViewImpl.getEntities

  override def getEntity(teamId: String): Future[Option[TeamProbability]] =
    TeamProbabilityRepository.batchViewImpl.getEntity(teamId)

  override def deleteEntity(entity: TeamProbability): Future[Boolean] = TeamProbabilityRepository.batchViewImpl.deleteEntity(entity)

  override def createTable: Future[Boolean] = TeamProbabilityRepository.batchViewImpl.createTable
}
