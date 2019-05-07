package services.soccer.impl.pseudomaster

import domain.soccer.Rating
import repositories.soccer.RatingRepository
import services.soccer.RatingService

import scala.concurrent.Future

class RatingServiceImpl extends RatingService {
  override def getTeamRatings(teamId: String): Future[Seq[Rating]] =
    RatingRepository.pseudomasterImpl.getTeamRatings(teamId)

  override def saveEntity(entity: Rating): Future[Boolean] =
    RatingRepository.pseudomasterImpl.saveEntity(entity)

  override def getEntities: Future[Seq[Rating]] =
    RatingRepository.pseudomasterImpl.getEntities

  override def getEntity(teamId: String): Future[Option[Rating]] = RatingRepository.pseudomasterImpl.getEntity(teamId)

  override def createTable: Future[Boolean] = RatingRepository.pseudomasterImpl.createTable

  override def deleteEntity(entity: Rating): Future[Boolean] =
    RatingRepository.pseudomasterImpl.deleteEntity(entity)

  override def clear: Future[Boolean] = RatingRepository.pseudomasterImpl.clear
}
