package services.soccer.impl.master

import domain.soccer.Head2Head
import repositories.soccer.Head2HeadRepository
import services.soccer.Head2HeadService

import scala.concurrent.Future

class Head2HeadServiceImpl extends Head2HeadService {

  override def saveEntity(entity: Head2Head): Future[Boolean] =
    Head2HeadRepository.masterImpl.saveEntity(entity)

  override def getEntities: Future[Seq[Head2Head]] = Head2HeadRepository.masterImpl.getEntities

  override def getHomeTeamMatches(homeTeamId: String): Future[Seq[Head2Head]] =
    Head2HeadRepository.masterImpl.getHomeTeamMatches(homeTeamId)

  override def getEntity(homeTeamId: String): Future[Option[Head2Head]] =
    Head2HeadRepository.masterImpl.getEntity(homeTeamId)

  override def createTable: Future[Boolean] = Head2HeadRepository.masterImpl.createTable

  override def deleteEntity(entity: Head2Head): Future[Boolean] = Head2HeadRepository.masterImpl.deleteEntity(entity)
}
