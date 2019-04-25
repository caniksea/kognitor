package services.soccer.impl.pseudomaster

import component.soccer.SoccerComponent
import domain.soccer.Team
import repositories.soccer.TeamRepository
import services.soccer.TeamService

import scala.concurrent.Future

class TeamServiceImpl extends TeamService {
  override def saveEntity(entity: Team): Future[Boolean] =
    TeamRepository.pseudomasterImpl.saveEntity(entity)

  override def getEntities: Future[Seq[Team]] =
    TeamRepository.pseudomasterImpl.getEntities

  override def getEntity(teamId: String): Future[Option[Team]] =
    TeamRepository.pseudomasterImpl.getEntity(teamId)

  override def createTable: Future[Boolean] =
    TeamRepository.pseudomasterImpl.createTable

  override def deleteEntity(entity: Team): Future[Boolean] =
    TeamRepository.pseudomasterImpl.deleteEntity(entity)

  override def findByName(teamName: String): Future[Option[Team]] = SoccerComponent.findByName(teamName, TeamService.pseudomasterImpl)
}
