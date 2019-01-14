package repositories

import scala.concurrent.Future

trait Repository[R] {

  def saveEntity(entity: R): Future[Boolean]

  def getEntity(id: String): Future[Option[R]]

  def createTable: Future[Boolean]

}
