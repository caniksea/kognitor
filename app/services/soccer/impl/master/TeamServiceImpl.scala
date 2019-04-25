package services.soccer.impl.master

import component.soccer.SoccerComponent
import domain.soccer.Team
import repositories.soccer.TeamRepository
import services.soccer.TeamService

import scala.concurrent.Future

class TeamServiceImpl extends TeamService {
  override def saveEntity(entity: Team): Future[Boolean] =
    TeamRepository.masterImpl.saveEntity(entity)

  override def getEntities: Future[Seq[Team]] =
    TeamRepository.masterImpl.getEntities

  override def getEntity(teamId: String): Future[Option[Team]] =
    TeamRepository.masterImpl.getEntity(teamId)

  override def createTable: Future[Boolean] =
    TeamRepository.masterImpl.createTable

  override def deleteEntity(entity: Team): Future[Boolean] =
    TeamRepository.masterImpl.deleteEntity(entity)

  override def findByName(teamName: String): Future[Option[Team]] = SoccerComponent.findByName(teamName, TeamService.masterImpl)
}
