package component.feeder

import domain.feeder.AppDatasource
import services.feeder.AppDatasourceService

import com.softwaremill.sttp._
import com.softwaremill.sttp.playJson._
import com.softwaremill.sttp.asynchttpclient.future.AsyncHttpClientFutureBackend
import conf.connections.NetConnection
import domain.datainjection.Post
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object CrawlerComponent {

  implicit lazy val sttpBackend = AsyncHttpClientFutureBackend()
  lazy val className = "CrawlerComponent "
  lazy val conn: NetConnection.type = NetConnection
  lazy val secure: Boolean = conn.schema
  val scheme: String = if (secure) "https" else "http"

  def crawl(source: String) = {
    lazy val url = uri"${source}"
    val apiResponse: Future[Response[String]] =
      sttp
        .post(url)
//        .body(data)
        .response(asString)
        .send()
    val response = apiResponse map (result => Json.fromJson[Post](Json.parse(result.unsafeBody)).asEither)
    response map (result => processPost(source, result))
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
