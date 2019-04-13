package services.feeder

import java.time.LocalDate

import domain.feeder.RatingFeeder
import services.CRUDService
import services.feeder.impl.RatingFeederServiceImpl

import scala.concurrent.Future

trait RatingFeederService extends CRUDService[RatingFeeder] {
  def getTeamsRating(teamNames: List[String], today: LocalDate): Future[Seq[RatingFeeder]]

  def getTeamRating(teamName: String, date: LocalDate): Future[Option[RatingFeeder]]
}

object RatingFeederService {
  def apply: RatingFeederService = new RatingFeederServiceImpl()
}
