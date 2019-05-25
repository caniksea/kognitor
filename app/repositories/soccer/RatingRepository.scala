package repositories.soccer

import domain.soccer.Rating
import repositories.CRUDRepository
import repositories.soccer.impl.cassandra.{master, pseudomaster}

import scala.concurrent.Future

trait RatingRepository extends CRUDRepository[Rating] {

  def getTeamRatings(teamId: String): Future[Seq[Rating]]
  def clear: Future[Boolean]
  def deleteByTeamId(teamId: String): Future[Boolean]
}

object RatingRepository {
  def masterImpl: RatingRepository = new master.RatingRepositoryImpl()
  def pseudomasterImpl: RatingRepository = new pseudomaster.RatingRepositoryImpl()
}
