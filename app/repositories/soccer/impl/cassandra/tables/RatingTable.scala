package repositories.soccer.impl.cassandra.tables

import java.time.LocalDate

import com.outworkers.phantom.dsl._
import com.outworkers.phantom.streams._
import com.outworkers.phantom.jdk8._
import domain.soccer.Rating

import scala.concurrent.Future

abstract class RatingTable extends Table[RatingTable, Rating] with RootConnector {

  override implicit lazy val tableName = "rating"

  object teamId extends StringColumn with PartitionKey

  object rating extends DoubleColumn

  object dateCreated extends Col[LocalDate] with PrimaryKey

  def saveEntity(rating: Rating): Future[ResultSet] = {
    insert
      .value(_.teamId, rating.teamId)
      .value(_.rating, rating.rating)
      .value(_.dateCreated, rating.dateCreated)
      .future()
  }

  def getTeamRatings(teamId: String): Future[Seq[Rating]] = {
    select
      .where(_.teamId eqs teamId)
      .fetchEnumerator() run Iteratee.collect()
  }

  // remove in production
  def deleteEntity(entity: Rating): Future[ResultSet] = {
    delete
      .where(_.teamId eqs entity.teamId)
      .future()
  }

  def clear: Future[ResultSet] = {
    truncate.future()
  }

  def deleteByTeamId(teamId: String): Future[ResultSet] = {
    delete
      .where(_.teamId eqs teamId)
      .future()
  }

  def getTeamRatingForDate(teamId: String, date: LocalDate): Future[Seq[Rating]] = {
    select
      .where(_.teamId eqs teamId)
      .and(_.dateCreated eqs date)
      .fetchEnumerator() run Iteratee.collect()
  }

}
