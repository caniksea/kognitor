package repositories.feeder

import domain.feeder.AppDatasource
import repositories.CRUDRepository
import repositories.feeder.impl.cassandra.AppDatasourceRepositoryImpl

import scala.concurrent.Future

trait AppDatasourceRepository extends CRUDRepository[AppDatasource] {

  def getSourcesForEvent(event: String): Future[Seq[AppDatasource]]

}

object AppDatasourceRepository {
  def apply: AppDatasourceRepository = new AppDatasourceRepositoryImpl()
}
