package repositories.batch.master.impl

import repositories.batch.master.TeamRepository

import scala.concurrent.Future

class TeamRepositoryImpl extends TeamRepository {
  override def saveEntity(entity: TeamRepository): Future[Boolean] = ???

  override def getEntity(id: String): Future[Option[TeamRepository]] = ???
}
