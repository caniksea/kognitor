package services.feeder

import scala.concurrent.Future

trait CrawlerService {
  def getDataForEvent(event: String): Future[Boolean]
}
