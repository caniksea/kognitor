package repositories.feeder.impl.cassandra

import java.time.LocalDate

import com.outworkers.phantom.connectors.KeySpace
import com.outworkers.phantom.database.Database
import com.outworkers.phantom.dsl._
import conf.connections.DataConnection
import domain.feeder.FormFeeder
import repositories.feeder.FormFeederRepository
import repositories.feeder.impl.cassandra.table.FormFeederTable

import scala.concurrent.Future

class FormFeederRepositoryImpl extends FormFeederRepository {
  override def getTeamForm(teamName: String, date: LocalDate): Future[Option[FormFeeder]] =
    FormFeederDatabase.FormFeederTable.getTeamForm(teamName, date)

  override def saveEntity(entity: FormFeeder): Future[Boolean] =
    FormFeederDatabase.FormFeederTable.saveEntity(entity).map(result => result.isExhausted())

  override def getEntity(id: String): Future[Option[FormFeeder]] = ???

  override def getEntities: Future[Seq[FormFeeder]] = ???

  override def deleteEntity(entity: FormFeeder): Future[Boolean] = ???

  override def createTable: Future[Boolean] = {
    implicit  def keySpace: KeySpace = DataConnection.masterKeyspaceQuery.keySpace
    implicit  def session: Session = DataConnection.masterConnector.session

    FormFeederDatabase.FormFeederTable.create.ifNotExists().future().map(result => result.head.isExhausted())
  }
}

class FormFeederDatabase(override val connector: KeySpaceDef) extends Database[FormFeederDatabase](connector) {
  object FormFeederTable extends FormFeederTable with connector.Connector
}

object FormFeederDatabase extends FormFeederDatabase(DataConnection.masterConnector)
