package repositories.soccer.impl.cassandra.pseudomaster

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
    Head2HeadDatabase.FixtureTable$.saveEntity(entity).map(result => result.isExhausted())

  override def getHomeTeamMatches(homeTeamId: String): Future[Seq[Fixture]] =
    Head2HeadDatabase.FixtureTable$.getHomeTeamMatches(homeTeamId)

  override def getEntity(id: String): Future[Option[Fixture]] = ???

  override def getEntities: Future[Seq[Fixture]] = ???

  override def createTable: Future[Boolean] = {
    implicit def keyspace: KeySpace = DataConnection.pseudomasterKeyspaceQuery.keySpace

    implicit def session: Session = DataConnection.pseudomasterConnector.session

    Head2HeadDatabase.FixtureTable$.create.ifNotExists().future().map(result => result.head.isExhausted())
  }

  override def deleteEntity(entity: Fixture): Future[Boolean] =
    Head2HeadDatabase.FixtureTable$.deleteEntity(entity).map(result => result.isExhausted())
}

class Head2HeadDatabase(override val connector: KeySpaceDef) extends Database[Head2HeadDatabase](connector) {
  object FixtureTable$ extends FixtureTable with connector.Connector
}

object Head2HeadDatabase extends Head2HeadDatabase(DataConnection.pseudomasterConnector)
