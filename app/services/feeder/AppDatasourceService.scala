package services.feeder

import domain.feeder.AppDatasource
import services.CRUDService
import services.feeder.impl.AppDatasourceServiceImpl

import scala.concurrent.Future

trait AppDatasourceService extends CRUDService[AppDatasource] {
  def getSourcesForEvent(event: String): Future[Seq[AppDatasource]]
}

object AppDatasourceService {
  def apply: AppDatasourceService = new AppDatasourceServiceImpl()
}
