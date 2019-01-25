package repositories.soccer.impl.cassandra.master

import com.outworkers.phantom.connectors.KeySpace
import com.outworkers.phantom.database.Database
import com.outworkers.phantom.dsl._
import conf.connections.DataConnection
import domain.soccer.Head2Head
import repositories.soccer.Head2HeadRepository
import repositories.soccer.impl.cassandra.tables.Head2HeadTable

import scala.concurrent.Future

class Head2HeadRepositoryImpl extends Head2HeadRepository{

  override def saveEntity(entity: Head2Head): Future[Boolean] =
    Head2HeadDatabase.Head2HeadTable.saveEntity(entity).map(result => result.isExhausted())

  override def getHomeTeamMatches(homeTeamId: String): Future[Seq[Head2Head]] =
    Head2HeadDatabase.Head2HeadTable.getHomeTeamMatches(homeTeamId)

  override def getEntity(id: String): Future[Option[Head2Head]] = ???

  override def getEntities: Future[Seq[Head2Head]] = ???

  override def createTable: Future[Boolean] = {
    implicit def keySpace: KeySpace = DataConnection.masterKeyspaceQuery.keySpace

    implicit def session: Session = DataConnection.masterConnector.session

    Head2HeadDatabase.Head2HeadTable.create.ifNotExists().future().map(result => result.head.isExhausted())
  }

  override def deleteEntity(entity: Head2Head): Future[Boolean] =
    Head2HeadDatabase.Head2HeadTable.deleteEntity(entity).map(result => result.isExhausted())
}

class Head2HeadDatabase(override val connector: KeySpaceDef) extends Database[Head2HeadDatabase](connector) {
  object Head2HeadTable extends Head2HeadTable with connector.Connector
}

object Head2HeadDatabase extends Head2HeadDatabase(DataConnection.masterConnector)
