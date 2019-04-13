package repositories.feeder

import java.time.{LocalDate}

import domain.feeder.RatingFeeder
import repositories.CRUDRepository
import repositories.feeder.impl.cassandra.RatingFeederRepositoryImpl

import scala.concurrent.Future

trait RatingFeederRepository extends CRUDRepository[RatingFeeder] {
  def getTeamsRating(teamNames: List[String], today: LocalDate): Future[Seq[RatingFeeder]]
  def getTeamRating(teamName: String, date: LocalDate): Future[Option[RatingFeeder]]
}

object RatingFeederRepository {
  def apply: RatingFeederRepository = new RatingFeederRepositoryImpl()
}
