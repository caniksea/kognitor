package repositories.batch.impl.cassandra.master

import com.outworkers.phantom.dsl._
import conf.connections.DataConnection
import domain.soccer.{Rating, Team}
import repositories.batch.TeamRepository
import repositories.batch.impl.cassandra.tables.TeamTable

import scala.concurrent.Future

class TeamRepositoryImpl extends TeamRepository {

  override def saveEntity(entity: Team): Future[Boolean] =
    TeamDatabase.TeamTable.saveEntity(entity).map(result => result.isExhausted())

  override def getEntity(teamId: String): Future[Option[Team]] =
    TeamDatabase.TeamTable.getEntity(teamId)

  override def getEntities: Future[Seq[Team]] = ???

  override def createTable: Future[Boolean] = {
    implicit def keyspace: KeySpace = DataConnection.masterKeyspaceQuery.keySpace

    implicit def session: Session = DataConnection.masterConnector.session

    TeamDatabase.TeamTable.create.ifNotExists().future().map(result => result.head.isExhausted())
  }
}

class TeamDatabase(override val connector: KeySpaceDef) extends Database[TeamDatabase](connector) {
  object TeamTable extends TeamTable with connector.Connector
}

object TeamDatabase extends TeamDatabase(DataConnection.masterConnector)
