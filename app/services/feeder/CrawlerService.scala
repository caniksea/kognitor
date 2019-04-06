package services.feeder

import services.feeder.impl.CrawlerServiceImpl

import scala.concurrent.Future

trait CrawlerService {
  def getDataForEvent(event: String): Future[Seq[Option[Any]]]
}

object CrawlerService{
  def apply: CrawlerService = new CrawlerServiceImpl()
}
