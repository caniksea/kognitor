package repositories.soccer.impl.cassandra.tables

import com.outworkers.phantom.dsl._
import com.outworkers.phantom.streams._
import com.outworkers.phantom.jdk8._
import domain.soccer.TeamProbability

import scala.concurrent.Future

abstract class TeamProbabilityTable extends Table[TeamProbabilityTable, TeamProbability] with RootConnector {

  override lazy val tableName = "teamprobability"

  object teamId extends StringColumn with PartitionKey

  object head2headHomeWinsProbability extends DoubleColumn

  object ratingProbability extends DoubleColumn

  object formProbability extends DoubleColumn

  def saveEntity(entity: TeamProbability): Future[ResultSet] = {
    insert
      .value(_.teamId, entity.teamId)
      .value(_.head2headHomeWinsProbability, entity.head2headHomeWinsProbability)
      .value(_.ratingProbability, entity.ratingProbability)
      .value(_.formProbability, entity.formProbability)
      .future()
  }

  def getEntity(teamId: String): Future[Option[TeamProbability]] = {
    select
      .where(_.teamId eqs teamId)
      .one()
  }

  def getEntities(): Future[Seq[TeamProbability]] = {
    select
      .fetchEnumerator() run Iteratee.collect()
  }

  def deleteEntity(entity: TeamProbability): Future[ResultSet] = {
    delete
      .where(_.teamId eqs entity.teamId)
      .future()
  }

}
