package repositories.feeder.impl.cassandra

import com.outworkers.phantom.database.Database
import com.outworkers.phantom.dsl._
import com.outworkers.phantom.connectors.KeySpace
import conf.connections.DataConnection
import domain.feeder.AppDatasource
import repositories.feeder.AppDatasourceRepository
import repositories.feeder.impl.cassandra.table.AppDatasourceTable

import scala.concurrent.Future

class AppDatasourceRepositoryImpl extends AppDatasourceRepository {
  override def getSourcesForEvent(event: String): Future[Seq[AppDatasource]] =
    AppDatasourceDatabase.AppDatasourceTable.getEventSources(event)

  override def saveEntity(entity: AppDatasource): Future[Boolean] = ???

  override def getEntity(id: String): Future[Option[AppDatasource]] = ???

  override def getEntities: Future[Seq[AppDatasource]] = ???

  override def deleteEntity(entity: AppDatasource): Future[Boolean] = ???

  override def createTable: Future[Boolean] = {
    implicit  def keySpace: KeySpace = DataConnection.masterKeyspaceQuery.keySpace
    implicit  def session: Session = DataConnection.masterConnector.session

    AppDatasourceDatabase.AppDatasourceTable.create.ifNotExists().future().map(result => result.head.isExhausted())
  }
}

class AppDatasourceDatabase(override val connector: KeySpaceDef) extends Database[AppDatasourceDatabase](connector) {
  object AppDatasourceTable extends AppDatasourceTable with connector.Connector
}

object AppDatasourceDatabase extends AppDatasourceDatabase(DataConnection.masterConnector)
