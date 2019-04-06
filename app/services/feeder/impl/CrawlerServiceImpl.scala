package services.feeder.impl

import component.feeder.CrawlerComponent
import services.feeder.CrawlerService

import scala.concurrent.Future

class CrawlerServiceImpl extends CrawlerService {
  override def getDataForEvent(event: String) = ??? //CrawlerComponent.getDataForEvent(event)
}
