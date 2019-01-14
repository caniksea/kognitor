package repositories.batch.master.impl

import repositories.batch.master.Head2HeadRepository
import com.outworkers.phantom.dsl._
import conf.connections.DataConnection
import domain.soccer.Head2Head
import repositories.batch.master.impl.cassandra.tables.Head2HeadTable

import scala.concurrent.Future

class Head2HeadRepositoryImpl extends Head2HeadRepository{

  override def saveEntity(entity: Head2Head): Future[Boolean] =
    Head2HeadDatabase.Head2HeadTable.saveEntity(entity).map(result => result.isExhausted())

  override def getHomeTeamMatches(homeTeamId: String): Future[Seq[Head2Head]] =
    Head2HeadDatabase.Head2HeadTable.getHomeTeamMatches(homeTeamId)

  override def getEntity(id: String): Future[Option[Head2Head]] = ???

  override def createTable: Future[Boolean] = {
    implicit def keyspace: KeySpace = DataConnection.masterKeyspaceQuery.keySpace

    implicit def session: Session = DataConnection.masterConnector.session

    Head2HeadDatabase.Head2HeadTable.create.ifNotExists().future().map(result => result.head.isExhausted())
  }
}

class Head2HeadDatabase(override val connector: KeySpaceDef) extends Database[Head2HeadDatabase](connector) {
  object Head2HeadTable extends Head2HeadTable with connector.Connector
}

object Head2HeadDatabase extends Head2HeadDatabase(DataConnection.masterConnector)
