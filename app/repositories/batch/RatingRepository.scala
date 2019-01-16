package repositories.batch

import domain.soccer.Rating
import repositories.CRUDRepository

import scala.concurrent.Future

trait RatingRepository extends CRUDRepository[Rating] {

  def getTeamRatings(teamId: String): Future[Seq[Rating]]
}
