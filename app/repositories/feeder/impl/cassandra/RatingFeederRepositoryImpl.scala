package repositories.feeder.impl.cassandra

import java.time.LocalDate

import com.outworkers.phantom.connectors.KeySpace
import com.outworkers.phantom.database.Database
import com.outworkers.phantom.dsl._
import conf.connections.DataConnection
import domain.feeder.RatingFeeder
import repositories.feeder.RatingFeederRepository
import repositories.feeder.impl.cassandra.table.RatingFeederTable

import scala.concurrent.Future

class RatingFeederRepositoryImpl extends RatingFeederRepository {

  override def saveEntity(entity: RatingFeeder): Future[Boolean] =
    RatingFeederDatabase.RatingFeederTable.saveEntity(entity).map(result => result.isExhausted())

  override def getEntity(id: String): Future[Option[RatingFeeder]] = ???

  override def getEntities: Future[Seq[RatingFeeder]] = ???

  override def deleteEntity(entity: RatingFeeder): Future[Boolean] = ???

  override def createTable: Future[Boolean] = {
    implicit  def keySpace: KeySpace = DataConnection.masterKeyspaceQuery.keySpace
    implicit  def session: Session = DataConnection.masterConnector.session

    RatingFeederDatabase.RatingFeederTable.create.ifNotExists().future().map(result => result.head.isExhausted())
  }

  override def getTeamRating(teamName: String, date: LocalDate): Future[Option[RatingFeeder]] =
    RatingFeederDatabase.RatingFeederTable.getTeamRating(teamName, date)

  override def getTeamsRating(teamNames: List[String], today: LocalDate): Future[Seq[RatingFeeder]] =
    RatingFeederDatabase.RatingFeederTable.getTeamsRating(teamNames, today)
}

class RatingFeederDatabase(override val connector: KeySpaceDef) extends Database[RatingFeederDatabase](connector) {
  object RatingFeederTable extends RatingFeederTable with connector.Connector
}

object RatingFeederDatabase extends RatingFeederDatabase(DataConnection.masterConnector)