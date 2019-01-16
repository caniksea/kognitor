package services.batch.impl.pseudomaster

import domain.soccer.Head2Head
import repositories.batch.Head2HeadRepository
import services.batch.Head2HeadService

import scala.concurrent.Future

class Head2HeadServiceImpl extends Head2HeadService {
  override def saveEntity(entity: Head2Head): Future[Boolean] =
    Head2HeadRepository.pseudomasterImpl.saveEntity(entity)

  override def getEntities: Future[Seq[Head2Head]] = Head2HeadRepository.pseudomasterImpl.getEntities

  override def getHomeTeamMatches(homeTeamId: String): Future[Seq[Head2Head]] =
    Head2HeadRepository.pseudomasterImpl.getHomeTeamMatches(homeTeamId)

  override def getEntity(homeTeamId: String): Future[Option[Head2Head]] =
    Head2HeadRepository.pseudomasterImpl.getEntity(homeTeamId)

  override def createTable: Future[Boolean] = Head2HeadRepository.pseudomasterImpl.createTable
}
