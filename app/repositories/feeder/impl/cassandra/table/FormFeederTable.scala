package repositories.feeder.impl.cassandra.table

import java.time.LocalDate

import com.outworkers.phantom.dsl._
import com.outworkers.phantom.streams._
import com.outworkers.phantom.jdk8._
import domain.feeder.FormFeeder

import scala.concurrent.Future

abstract class FormFeederTable extends Table[FormFeederTable, FormFeeder] with RootConnector {

  override lazy val tableName = "formfeeder"

  object teamName extends StringColumn with PartitionKey

  object numberOfWins extends IntColumn

  object numberOfLoses extends IntColumn

  object numberOfDraws extends IntColumn

  object sourceDate extends Col[LocalDate] with PrimaryKey

  def saveEntity(entity: FormFeeder): Future[ResultSet] = {
    insert
      .value(_.teamName, entity.teamName)
      .value(_.numberOfDraws, entity.numberOfDraws)
      .value(_.numberOfLoses, entity.numberOfLoses)
      .value(_.numberOfWins, entity.numberOfWins)
      .value(_.sourceDate, entity.sourceDate)
      .future()
  }

  def getTeamForm(teamName: String, date: LocalDate): Future[Option[FormFeeder]] = {
    select
      .where(_.teamName eqs teamName)
      .and(_.sourceDate eqs date)
      .one()
  }

  def getTeamsForm(teamNames: List[String], date: LocalDate): Future[Seq[FormFeeder]] = {
    select
      .where(_.teamName in teamNames)
      .and(_.sourceDate eqs date)
      .fetchEnumerator() run Iteratee.collect()
  }
}
