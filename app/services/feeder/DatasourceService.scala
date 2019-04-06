package services.feeder

import domain.feeder.FeederData
import services.feeder.impl.DatasourceServiceImpl

import scala.concurrent.Future

trait DatasourceService {
  def getData(): Future[Seq[FeederData]]
}

object DatasourceService {
  def apply: DatasourceService = new DatasourceServiceImpl()
}
