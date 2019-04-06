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

  object dateCreated extends Col[LocalDate] with PrimaryKey

  def saveEntity(entity: FormFeeder): Future[ResultSet] = {
    insert
      .value(_.teamName, entity.teamName)
      .value(_.numberOfDraws, entity.numberOfDraws)
      .value(_.numberOfLoses, entity.numberOfLoses)
      .value(_.numberOfWins, entity.numberOfWins)
      .value(_.dateCreated, entity.dateCreated)
      .future()
  }

  def getTeamForm(teamName: String, date: LocalDate): Future[Option[FormFeeder]] = {
    select
      .where(_.teamName eqs teamName)
      .and(_.dateCreated eqs date)
      .one()
  }
}
