package repositories.feeder.impl.cassandra.table

import java.time

import com.outworkers.phantom.dsl._
import com.outworkers.phantom.streams._
import com.outworkers.phantom.jdk8._
import domain.feeder.FixtureFeeder

import scala.concurrent.Future

abstract class FixtureFeederTable extends Table[FixtureFeederTable, FixtureFeeder] with RootConnector {

  override lazy val tableName = "fixturefeeder"

  object teamName extends StringColumn with PartitionKey

  object awayTeamName extends StringColumn

  object homeTeamGoals extends IntColumn

  object awayTeamGoals extends IntColumn

  object dateCreated extends Col[time.LocalDate] with PrimaryKey

  def saveEntity(entity: FixtureFeeder): Future[ResultSet] = {
    insert
      .value(_.teamName, entity.teamName)
      .value(_.awayTeamName, entity.awayTeamName)
      .value(_.homeTeamGoals, entity.homeTeamGoals)
      .value(_.awayTeamGoals, entity.awayTeamGoals)
      .value(_.dateCreated, entity.dateCreated)
      .future()
  }

  def getTeamFixture(teamName: String, date: time.LocalDate): Future[Option[FixtureFeeder]] = {
    select
      .where(_.teamName eqs teamName)
      .and(_.dateCreated eqs date)
      .one()
  }

  def getTeamsFixture(teamSea: List[String], date: time.LocalDate): Future[Seq[FixtureFeeder]] = {
    select
      .where(_.teamName in teamSea)
      .and(_.dateCreated eqs date)
      .fetchEnumerator() run Iteratee.collect()
  }

}
