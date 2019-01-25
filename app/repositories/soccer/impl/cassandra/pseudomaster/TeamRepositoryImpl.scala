package repositories.soccer.impl.cassandra.pseudomaster

import com.outworkers.phantom.connectors.KeySpace
import com.outworkers.phantom.database.Database
import com.outworkers.phantom.dsl._
import conf.connections.DataConnection
import domain.soccer.Team
import repositories.soccer.TeamRepository
import repositories.soccer.impl.cassandra.tables.TeamTable

import scala.concurrent.Future

class TeamRepositoryImpl extends TeamRepository {

  override def saveEntity(entity: Team): Future[Boolean] =
    TeamDatabase.TeamTable.saveEntity(entity).map(result => result.isExhausted())

  override def getEntity(teamId: String): Future[Option[Team]] =
    TeamDatabase.TeamTable.getEntity(teamId)

  override def getEntities: Future[Seq[Team]] = ???

  override def createTable: Future[Boolean] = {
    implicit def keyspace: KeySpace = DataConnection.pseudomasterKeyspaceQuery.keySpace

    implicit def session: Session = DataConnection.pseudomasterConnector.session

    TeamDatabase.TeamTable.create.ifNotExists().future().map(result => result.head.isExhausted())
  }

  override def deleteEntity(entity: Team): Future[Boolean] =
    TeamDatabase.TeamTable.deleteEntity(entity).map(result => result.isExhausted())
}

class TeamDatabase(override val connector: KeySpaceDef) extends Database[TeamDatabase](connector) {
  object TeamTable extends TeamTable with connector.Connector
}

object TeamDatabase extends TeamDatabase(DataConnection.pseudomasterConnector)
