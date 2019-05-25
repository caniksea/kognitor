package repositories.soccer.impl.cassandra.master

import com.outworkers.phantom.connectors.KeySpace
import com.outworkers.phantom.database.Database
import com.outworkers.phantom.dsl._
import conf.connections.DataConnection
import domain.soccer.Fixture
import repositories.soccer.FixtureRepository
import repositories.soccer.impl.cassandra.tables.FixtureTable

import scala.concurrent.Future

class FixtureRepositoryImpl extends FixtureRepository{

  override def saveEntity(entity: Fixture): Future[Boolean] =
    FixtureDatabase.FixtureTable.saveEntity(entity).map(result => result.isExhausted())

  override def getHomeTeamMatches(homeTeamId: String): Future[Seq[Fixture]] =
    FixtureDatabase.FixtureTable.getHomeTeamMatches(homeTeamId)

  override def getEntity(id: String): Future[Option[Fixture]] = ???

  override def getEntities: Future[Seq[Fixture]] = ???

  override def createTable: Future[Boolean] = {
    implicit def keySpace: KeySpace = DataConnection.masterKeyspaceQuery.keySpace

    implicit def session: Session = DataConnection.masterConnector.session

    FixtureDatabase.FixtureTable.create.ifNotExists().future().map(result => result.head.isExhausted())
  }

  override def deleteEntity(entity: Fixture): Future[Boolean] =
    FixtureDatabase.FixtureTable.deleteEntity(entity).map(result => result.isExhausted())

  override def clear: Future[Boolean] = ???

  override def deleteByTeamId(teamId: String): Future[Boolean] = ???
}

class FixtureDatabase(override val connector: KeySpaceDef) extends Database[FixtureDatabase](connector) {
  object FixtureTable extends FixtureTable with connector.Connector
}

object FixtureDatabase extends FixtureDatabase(DataConnection.masterConnector)
