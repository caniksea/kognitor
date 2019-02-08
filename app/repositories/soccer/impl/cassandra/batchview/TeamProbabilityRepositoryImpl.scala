package repositories.soccer.impl.cassandra.batchview

import com.outworkers.phantom.connectors.KeySpace
import com.outworkers.phantom.database.Database
import com.outworkers.phantom.dsl._
import conf.connections.DataConnection
import domain.soccer.TeamProbability
import repositories.soccer.TeamProbabilityRepository
import repositories.soccer.impl.cassandra.tables.TeamProbabilityTable

import scala.concurrent.Future

class TeamProbabilityRepositoryImpl extends TeamProbabilityRepository {
  override def saveEntity(entity: TeamProbability): Future[Boolean] =
    TeamProbabilityDatabase.TeamProbabilityTable.saveEntity(entity).map(result => result.isExhausted())

  override def getEntity(teamId: String): Future[Option[TeamProbability]] =
    TeamProbabilityDatabase.TeamProbabilityTable.getEntity(teamId)

  override def getEntities: Future[Seq[TeamProbability]] =
    TeamProbabilityDatabase.TeamProbabilityTable.getEntities()

  override def deleteEntity(entity: TeamProbability): Future[Boolean] =
    TeamProbabilityDatabase.TeamProbabilityTable.deleteEntity(entity).map(result => result.isExhausted())

  override def createTable: Future[Boolean] = {
    implicit def keyspace: KeySpace = DataConnection.batchViewKeyspaceQuery.keySpace

    implicit def session: Session = DataConnection.batchViewConnector.session

    TeamProbabilityDatabase.TeamProbabilityTable.create.ifNotExists().future().map(result => result.head.isExhausted())
  }
}

class TeamProbabilityDatabase(override val connector: KeySpaceDef) extends Database[TeamProbabilityDatabase](connector) {
  object TeamProbabilityTable extends TeamProbabilityTable with connector.Connector
}

object TeamProbabilityDatabase extends TeamProbabilityDatabase(DataConnection.batchViewConnector)
