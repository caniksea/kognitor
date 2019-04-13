package repositories.feeder.impl.cassandra

import java.time.LocalDate

import com.outworkers.phantom.connectors.KeySpace
import com.outworkers.phantom.database.Database
import com.outworkers.phantom.dsl._
import conf.connections.DataConnection
import domain.feeder
import domain.feeder.FixtureFeeder
import repositories.feeder.FixtureFeederRepository
import repositories.feeder.impl.cassandra.table.FixtureFeederTable

import scala.concurrent.Future

class FixtureFeederRepositoryImpl extends FixtureFeederRepository {
  override def getTeamFixture(teamName: String, date: LocalDate): Future[Option[feeder.FixtureFeeder]] =
    FixtureFeederDatabase.FixtureFeederTable.getTeamFixture(teamName, date)

  override def saveEntity(entity: feeder.FixtureFeeder): Future[Boolean] =
    FixtureFeederDatabase.FixtureFeederTable.saveEntity(entity).map(result => result.isExhausted())

  override def getEntity(id: String): Future[Option[feeder.FixtureFeeder]] = ???

  override def getEntities: Future[Seq[feeder.FixtureFeeder]] = ???

  override def deleteEntity(entity: feeder.FixtureFeeder): Future[Boolean] = ???

  override def createTable: Future[Boolean] = {
    implicit  def keySpace: KeySpace = DataConnection.masterKeyspaceQuery.keySpace
    implicit  def session: Session = DataConnection.masterConnector.session

    FixtureFeederDatabase.FixtureFeederTable.create.ifNotExists().future().map(result => result.head.isExhausted())
  }

  override def getTeamsFixture(teamList: List[String], date: LocalDate): Future[Seq[FixtureFeeder]] =
    FixtureFeederDatabase.FixtureFeederTable.getTeamsFixture(teamList, date)
}

class FixtureFeederDatabase(override val connector: KeySpaceDef) extends Database[FixtureFeederDatabase](connector) {
  object FixtureFeederTable extends FixtureFeederTable with connector.Connector
}

object FixtureFeederDatabase extends FixtureFeederDatabase(DataConnection.masterConnector)
