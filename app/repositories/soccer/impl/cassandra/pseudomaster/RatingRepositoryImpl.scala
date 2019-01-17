package repositories.soccer.impl.cassandra.pseudomaster

import com.outworkers.phantom.dsl._
import conf.connections.DataConnection
import domain.soccer.Rating
import repositories.soccer.RatingRepository
import repositories.soccer.impl.cassandra.tables.RatingTable

import scala.concurrent.Future

class RatingRepositoryImpl extends RatingRepository {

  override def saveEntity(entity: Rating): Future[Boolean] =
    RatingDatabase.RatingTable.saveEntity(entity).map(result => result.isExhausted())

  override def getEntity(id: String): Future[Option[Rating]] = ???

  override def getTeamRatings(teamId: String): Future[Seq[Rating]] =
    RatingDatabase.RatingTable.getTeamRatings(teamId)

  override def getEntities: Future[Seq[Rating]] = ???

  override def createTable: Future[Boolean] = {
    implicit def keyspace: KeySpace = DataConnection.pseudomasterKeyspaceQuery.keySpace

    implicit def session: Session = DataConnection.pseudomasterConnector.session

    RatingDatabase.RatingTable.create.ifNotExists().future().map(result => result.head.isExhausted())
  }
}

class RatingDatabase(override val connector: KeySpaceDef) extends Database[RatingDatabase](connector) {
  object RatingTable extends RatingTable with connector.Connector
}

object RatingDatabase extends RatingDatabase(DataConnection.pseudomasterConnector)
