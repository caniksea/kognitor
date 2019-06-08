package services.soccer

import java.time.LocalDate

import domain.soccer.Rating
import services.CRUDService
import services.soccer.impl.{master, pseudomaster}

import scala.concurrent.Future

trait RatingService extends CRUDService[Rating]{

  def getTeamRatingForDate(teamId: String, date: LocalDate = LocalDate.now): Future[Seq[Rating]]
  def getTeamRatings(teamId: String): Future[Seq[Rating]]
  def clear: Future[Boolean]
  def deleteByTeamId(teamId: String): Future[Boolean]
}

object RatingService {
  def masterImpl: RatingService = new master.RatingServiceImpl()
  def pseudomasterImpl: RatingService = new pseudomaster.RatingServiceImpl()
}
