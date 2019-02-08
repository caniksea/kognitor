package services.soccer

import domain.soccer.Rating
import services.CRUDService
import services.soccer.impl.{master, pseudomaster}

import scala.concurrent.Future

trait RatingService extends CRUDService[Rating]{
  def getTeamRatings(teamId: String): Future[Seq[Rating]]
}

object RatingService {
  def masterImpl: RatingService = new master.RatingServiceImpl()
  def pseudomasterImpl: RatingService = new pseudomaster.RatingServiceImpl()
}
