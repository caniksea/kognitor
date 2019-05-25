package repositories.soccer.impl.cassandra.tables

import java.time.{LocalDate, LocalDateTime}

import com.outworkers.phantom.dsl._
import com.outworkers.phantom.streams._
import com.outworkers.phantom.jdk8._
import domain.soccer.Fixture

import scala.concurrent.Future

abstract class FixtureTable extends Table[FixtureTable, Fixture] with RootConnector {

  override lazy val tableName = "fixture"

  object homeTeamId extends StringColumn with PartitionKey

  object awayTeamId extends StringColumn

  object homeTeamGoals extends IntColumn

  object awayTeamGoals extends IntColumn

  object dateCreated extends Col[LocalDate] with PrimaryKey

  def saveEntity(entity: Fixture): Future[ResultSet] = {
    insert
      .value(_.homeTeamId, entity.homeTeamId)
      .value(_.awayTeamId, entity.awayTeamId)
      .value(_.homeTeamGoals, entity.homeTeamGoals)
      .value(_.awayTeamGoals, entity.awayTeamGoals)
      .value(_.dateCreated, entity.dateCreated)
      .future()
  }

  def getHomeTeamMatches(homeTeamId: String): Future[Seq[Fixture]] = {
    select
      .where(_.homeTeamId eqs homeTeamId)
      .fetchEnumerator() run Iteratee.collect()
  }

//  def getHead2Head(homeTeamId: String, awayTeamId: String): Future[Seq[Head2Head]] = {
//    select
//      .where(_.homeTeamId eqs homeTeamId)
//      .and(_.awayTeamId eqs awayTeamId)
//      .fetchEnumerator() run Iteratee.collect()
//  }

  // remove in production
  def deleteEntity(entity: Fixture): Future[ResultSet] = {
    delete
      .where(_.homeTeamId eqs entity.homeTeamId)
      .future()
  }

  def clear: Future[ResultSet] = {
    truncate.future()
  }

  def deleteByTeamId(teamId: String): Future[ResultSet] = {
    delete
      .where(_.homeTeamId eqs teamId)
      .future()
  }

}
