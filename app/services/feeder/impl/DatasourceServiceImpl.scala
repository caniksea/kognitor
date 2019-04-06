package services.feeder.impl

import component.feeder.DatasourceComponent
import domain.feeder.FeederData
import services.feeder.DatasourceService

import scala.concurrent.Future

class DatasourceServiceImpl extends DatasourceService {
  override def getData(): Future[Seq[FeederData]] = DatasourceComponent.getData()
}
