package component.feeder

import com.softwaremill.sttp._
import com.softwaremill.sttp.playJson._
import com.softwaremill.sttp.asynchttpclient.future.AsyncHttpClientFutureBackend
import conf.connections.NetConnection
import domain.datainjection.Post
import play.api.libs.json.{JsPath, Json, JsonValidationError}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Deprecated
object CrawlerComponent {

//  implicit lazy val sttpBackend = AsyncHttpClientFutureBackend()
//  lazy val className = "CrawlerComponent "
//  lazy val conn: NetConnection.type = NetConnection
//  lazy val secure: Boolean = conn.schema
//  val scheme: String = if (secure) "https" else "http"
//
//  def processPost(source: String, result: Either[Seq[(JsPath, Seq[JsonValidationError])], Post]): Option[FeederResponse] = ???
//
//  def crawl(source: String) = {
//    lazy val url = uri"${source}"
//    val apiResponse: Future[Response[String]] =
//      sttp
//        .post(url)
////        .body(data)
//        .response(asString)
//        .send()
//    val response = apiResponse map (result => Json.fromJson[Post](Json.parse(result.unsafeBody)).asEither)
//    response map (result => processPost(source, result))
//  }
//
//  /**
//    * Perform crawl on each source and returns a sequence of response
//    * @param sources
//    * @return
//    */
//  def crawlSources(sources: Seq[Any]) = {
//    val result: collection.mutable.Buffer[Option[FeederResponse]] = collection.mutable.Buffer()
//    sources.foreach(source => {
//      val data = crawl(source.source)
//      data.map(d => result += d)
//    })
//    result.toSeq
//  }
//
//  def getDataForEvent(event: String) = {
//    for {
//      sources <- AppDatasourceService.apply.getSourcesForEvent(event)
//    } yield {
//      crawlSources(sources)
//    }
//  }

}
