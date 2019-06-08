package repositories.soccer.impl.cassandra.pseudomaster

import java.time

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
    implicit def keyspace: KeySpace = DataConnection.pseudomasterKeyspaceQuery.keySpace

    implicit def session: Session = DataConnection.pseudomasterConnector.session

    FixtureDatabase.FixtureTable.create.ifNotExists().future().map(result => result.head.isExhausted())
  }

  override def deleteEntity(entity: Fixture): Future[Boolean] =
    FixtureDatabase.FixtureTable.deleteEntity(entity).map(result => result.isExhausted())

  override def clear: Future[Boolean] = FixtureDatabase.FixtureTable.clear.map(result => result.isExhausted())

  override def deleteByTeamId(teamId: String): Future[Boolean] =
    FixtureDatabase.FixtureTable.deleteByTeamId(teamId).map(result => result.isExhausted())

  override def getTeamMatchForDate(teamId: String, date: time.LocalDate): Future[Seq[Fixture]] =
    FixtureDatabase.FixtureTable.getTeamMatchForDate(teamId, date)
}

class FixtureDatabase(override val connector: KeySpaceDef) extends Database[FixtureDatabase](connector) {
  object FixtureTable extends FixtureTable with connector.Connector
}

object FixtureDatabase extends FixtureDatabase(DataConnection.pseudomasterConnector)
