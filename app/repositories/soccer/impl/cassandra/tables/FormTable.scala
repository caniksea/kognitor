package repositories.soccer.impl.cassandra.tables

import java.time.{LocalDate, LocalDateTime}

import com.outworkers.phantom.dsl._
import com.outworkers.phantom.streams._
import com.outworkers.phantom.jdk8._
import domain.soccer.Form

import scala.concurrent.Future

abstract class FormTable extends Table[FormTable, Form] with RootConnector {

  override lazy val tableName = "form"

  object teamId extends StringColumn with PartitionKey

  object numberOfWins extends IntColumn

  object numberOfLoses extends IntColumn

  object numberOfDraws extends IntColumn

  object sourceDate extends Col[LocalDate] with PrimaryKey

  object dateCreated extends Col[LocalDateTime]

  def saveEntity(entity: Form): Future[ResultSet] = {
    insert
      .value(_.teamId, entity.teamId)
      .value(_.numberOfWins, entity.numberOfWins)
      .value(_.numberOfLoses, entity.numberOfLoses)
      .value(_.numberOfDraws, entity.numberOfDraws)
      .value(_.sourceDate, entity.sourceDate)
      .value(_.dateCreated, entity.dateCreated)
      .future()
  }

  def getTeamForms(teamId: String): Future[Seq[Form]] = {
    select
      .where(_.teamId eqs teamId)
      .fetchEnumerator() run Iteratee.collect()
  }

  def getEntities(): Future[Seq[Form]] = {
    select.fetchEnumerator() run Iteratee.collect()
  }

  // remove this in production
  def deleteEntity(entity: Form): Future[ResultSet] = {
    delete
      .where(_.teamId eqs entity.teamId)
      .future()
  }

  def clear: Future[ResultSet] = {
    truncate.future()
  }

}
