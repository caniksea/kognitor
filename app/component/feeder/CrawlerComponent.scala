package component.feeder

import domain.feeder.AppDatasource
import services.feeder.AppDatasourceService

import scala.concurrent.Future

object CrawlerComponent {

  def crawl(source: String) = {

  }

  def crawlSources(sources: Seq[AppDatasource]): Boolean = {
    sources.foreach(source => {
      val data = crawl(source.source)
    })
    true
  }

  def getDataForEvent(event: String): Future[Boolean] = {
    for {
      sources <- AppDatasourceService.apply.getSourcesForEvent(event)
    } yield {
      crawlSources(sources)
    }
  }

}
