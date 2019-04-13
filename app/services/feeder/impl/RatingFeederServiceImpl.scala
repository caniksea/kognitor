package services.feeder.impl

import java.time.LocalDate

import domain.feeder.RatingFeeder
import repositories.feeder.RatingFeederRepository
import services.feeder.RatingFeederService

import scala.concurrent.Future

class RatingFeederServiceImpl extends RatingFeederService {
  override def saveEntity(entity: RatingFeeder): Future[Boolean] =
    RatingFeederRepository.apply.saveEntity(entity)

  override def getEntities: Future[Seq[RatingFeeder]] = ???

  override def getEntity(id: String): Future[Option[RatingFeeder]] = ???

  override def deleteEntity(entity: RatingFeeder): Future[Boolean] = ???

  override def createTable: Future[Boolean] = RatingFeederRepository.apply.createTable

  override def getTeamRating(teamName: String, date: LocalDate): Future[Option[RatingFeeder]] = RatingFeederRepository.apply.getTeamRating(teamName, date)

  override def getTeamsRating(teamNames: List[String], today: LocalDate): Future[Seq[RatingFeeder]] =
    RatingFeederRepository.apply.getTeamsRating(teamNames, today)
}
