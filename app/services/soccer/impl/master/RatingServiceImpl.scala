package services.soccer.impl.master

import domain.soccer.Rating
import repositories.soccer.RatingRepository
import services.soccer.RatingService

import scala.concurrent.Future

class RatingServiceImpl extends RatingService {
  override def getTeamRatings(teamId: String): Future[Seq[Rating]] =
    RatingRepository.masterImpl.getTeamRatings(teamId)

  override def saveEntity(entity: Rating): Future[Boolean] =
    RatingRepository.masterImpl.saveEntity(entity)

  override def getEntities: Future[Seq[Rating]] =
    RatingRepository.masterImpl.getEntities

  override def getEntity(teamId: String): Future[Option[Rating]] = RatingRepository.masterImpl.getEntity(teamId)

  override def createTable: Future[Boolean] = RatingRepository.masterImpl.createTable

  override def deleteEntity(entity: Rating): Future[Boolean] = RatingRepository.masterImpl.deleteEntity(entity)

  override def clear: Future[Boolean] = ???
}
