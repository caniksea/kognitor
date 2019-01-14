package repositories.batch.master.impl.cassandra.tables

import java.time.LocalDateTime

import com.outworkers.phantom.dsl._
import com.outworkers.phantom.streams._
import domain.soccer.Head2Head

import scala.concurrent.Future

abstract class Head2HeadTable extends Table[Head2HeadTable, Head2Head] with RootConnector {

  override lazy val tableName = "head2head"

  object homeTeamId extends StringColumn with PartitionKey

  object awayTeamId extends StringColumn

  object homeTeamGoals extends IntColumn

  object awayTeamGoals extends IntColumn

  object dateOfCompetition extends Col[LocalDateTime]

  object dateCreated extends Col[LocalDateTime]

  def saveEntity(entity: Head2Head): Future[ResultSet] = {
    insert
      .value(_.homeTeamId, entity.homeTeamId)
      .value(_.awayTeamId, entity.awayTeamId)
      .value(_.homeTeamGoals, entity.homeTeamGoals)
      .value(_.awayTeamGoals, entity.awayTeamGoals)
      .value(_.dateOfCompetition, entity.dateOfCompetition)
      .value(_.dateCreated, entity.dateCreated)
      .future()
  }

  def getHomeTeamMatches(homeTeamId: String): Future[Seq[Head2Head]] = {
    select
      .where(_.homeTeamId eqs homeTeamId)
      .fetchEnumerator() run Iteratee.collect()
  }

}
