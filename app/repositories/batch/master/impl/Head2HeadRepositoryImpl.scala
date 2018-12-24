package repositories.batch.master.impl

import repositories.batch.master.Head2HeadRepository

import scala.concurrent.Future

class Head2HeadRepositoryImpl extends Head2HeadRepository{
  override def saveEntity(entity: Head2HeadRepository): Future[Boolean] = ???

  override def getEntity(id: String): Future[Option[Head2HeadRepository]] = ???
}
