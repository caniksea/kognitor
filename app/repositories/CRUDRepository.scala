package repositories

import scala.concurrent.Future

trait CRUDRepository[R] {

  def saveEntity(entity: R): Future[Boolean]

  def getEntity(id: String): Future[Option[R]]

  def getEntities: Future[Seq[R]]

  def deleteEntity(entity: R): Future[Boolean]

  def createTable: Future[Boolean]

}
