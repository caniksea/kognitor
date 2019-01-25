package repositories.soccer.impl.cassandra.tables

import com.outworkers.phantom.dsl._
import com.outworkers.phantom.streams._
import com.outworkers.phantom.jdk8._
import domain.soccer.Team

import scala.concurrent.Future

abstract class TeamTable extends Table[TeamTable, Team] with RootConnector {

  override implicit lazy val tableName = "team"

  object teamId extends StringColumn with PartitionKey

  object teamName extends StringColumn

  def saveEntity(entity: Team): Future[ResultSet] = {
    insert
      .value(_.teamId, entity.teamId)
      .value(_.teamName, entity.teamName)
      .future()
  }

  def getEntity(teamId: String): Future[Option[Team]] = {
    select
      .where(_.teamId eqs teamId)
      .one()
  }

  def getEntities: Future[Seq[Team]] = {
    select.fetchEnumerator() run Iteratee.collect()
  }

  // remove this in production.
  def deleteEntity(entity: Team): Future[ResultSet] = {
    delete
      .where(_.teamId eqs entity.teamId)
      .future()
  }

}
