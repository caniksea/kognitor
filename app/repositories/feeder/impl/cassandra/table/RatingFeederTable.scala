package repositories.feeder.impl.cassandra.table

import java.time.LocalDate

import com.outworkers.phantom.dsl._
import com.outworkers.phantom.streams._
import com.outworkers.phantom.jdk8._
import domain.feeder.RatingFeeder

import scala.concurrent.Future

abstract class RatingFeederTable extends Table[RatingFeederTable, RatingFeeder] with RootConnector {

  override lazy val tableName = "ratingfeeder"

  object teamName extends StringColumn with PartitionKey

  object rating extends DoubleColumn

  object dateCreated extends Col[LocalDate] with PrimaryKey

  def saveEntity(entity: RatingFeeder): Future[ResultSet] = {
    insert
      .value(_.teamName, entity.teamName)
      .value(_.rating, entity.rating)
      .value(_.dateCreated, entity.dateCreated)
      .future()
  }

  def getTeamRating(teamName: String, date: LocalDate): Future[Option[RatingFeeder]] = {
    select
      .where(_.teamName eqs teamName)
      .and(_.dateCreated eqs date)
      .one()
  }

}
