package repositories.soccer.impl.cassandra.pseudomaster

import com.outworkers.phantom.connectors.KeySpace
import com.outworkers.phantom.database.Database
import com.outworkers.phantom.dsl._
import conf.connections.DataConnection
import domain.soccer.Form
import repositories.soccer.FormRepository
import repositories.soccer.impl.cassandra.tables.FormTable

import scala.concurrent.Future

class FormRepositoryImpl extends FormRepository {
  override def saveEntity(entity: Form): Future[Boolean] =
    FormDatabase.FormTable.saveEntity(entity).map(result => result.isExhausted())

  override def getEntity(id: String): Future[Option[Form]] = ???

  override def getEntities: Future[Seq[Form]] = FormDatabase.FormTable.getEntities()

  override def getTeamForms(teamId: String): Future[Seq[Form]] = FormDatabase.FormTable.getTeamForms(teamId)

  override def createTable: Future[Boolean] = {
    implicit def keyspace: KeySpace = DataConnection.pseudomasterKeyspaceQuery.keySpace

    implicit def session: Session = DataConnection.pseudomasterConnector.session

    FormDatabase.FormTable.create.ifNotExists().future().map(result => result.head.isExhausted())

  }

  override def deleteEntity(entity: Form): Future[Boolean] =
    FormDatabase.FormTable.deleteEntity(entity).map(result => result.isExhausted())

  override def clear: Future[Boolean] = FormDatabase.FormTable.clear.map(result => result.isExhausted())

  override def deleteByTeamId(teamId: String): Future[Boolean] =
    FormDatabase.FormTable.deleteByTeamId(teamId).map(result => result.isExhausted())
}

class FormDatabase(override val connector: KeySpaceDef) extends Database[FormDatabase](connector) {
  object FormTable extends FormTable with connector.Connector
}

object FormDatabase extends FormDatabase(DataConnection.pseudomasterConnector)
