package repositories.soccer.impl.cassandra.tables

import com.outworkers.phantom.dsl._
import com.outworkers.phantom.streams._
import com.outworkers.phantom.jdk8._
import domain.soccer.TeamProbability

import scala.concurrent.Future

abstract class TeamProbabilityTable extends Table[TeamProbabilityTable, TeamProbability] with RootConnector {

  override lazy val tableName = "teamprobability"

  object teamId extends StringColumn with PartitionKey

  object winProbability extends DoubleColumn

  object goodRatingProbability extends DoubleColumn

  object badRatingProbability extends DoubleColumn

  object goodFormProbability extends DoubleColumn

  object badFormProbability extends DoubleColumn

  object goodHead2HeadProbability extends DoubleColumn

  object badHead2HeadProbability extends DoubleColumn

  def saveEntity(entity: TeamProbability): Future[ResultSet] = {
    insert
      .value(_.teamId, entity.teamId)
      .value(_.winProbability, entity.winProbability)
      .value(_.goodRatingProbability, entity.goodRatingProbability)
      .value(_.badRatingProbability, entity.badRatingProbability)
      .value(_.goodFormProbability, entity.goodFormProbability)
      .value(_.badFormProbability, entity.badFormProbability)
      .value(_.goodHead2HeadProbability, entity.goodHead2HeadProbability)
      .value(_.badHead2HeadProbability, entity.badHead2HeadProbability)
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
