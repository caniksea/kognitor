package services.datainjection.Impl

import domain.datainjection.{Link, MetaLink, Post}
import play.api.libs.json.{JsPath, Json, JsonValidationError}
import services.datainjection.PageService
import com.softwaremill.sttp._
import com.softwaremill.sttp.playJson._
import com.softwaremill.sttp.asynchttpclient.future.AsyncHttpClientFutureBackend
import conf.connections.NetConnection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PageServiceImpl extends PageService{

  implicit lazy val sttpBackend = AsyncHttpClientFutureBackend()
  lazy val className = "ArticleServiceImpl "
  lazy val conn: NetConnection.type = NetConnection
  lazy val secure: Boolean = conn.schema
  val scheme: String = if (secure) "https" else "http"
  lazy val baseUrl = uri"$scheme://${conn.crawler}/link/article"

  def getPost(link: Link): Future[Option[Post]] = {
    // Date Technical Debt. TO be removed once the Golang Fix is in Place
    val data = MetaLink(link.zone, "Today", link.linkHash,
      link.linkUrl, link.linkSite, link.linkTitle,
      link.linkType, link.linkSiteCode)
    // Date Technical Debt. TO be removed once the Golang Fix is in Place
    val apiResponse: Future[Response[String]] =
      sttp
        .post(baseUrl)
        .body(data)
        .response(asString)
        .send()
    val response = apiResponse map (result => Json.fromJson[Post](Json.parse(result.unsafeBody)).asEither)
    response map (result => processPost(link, result))
  }

  private def processPost(link: Link, post: Either[Seq[(JsPath, Seq[JsonValidationError])], Post]): Option[Post] = {
    post match {
      case Right(value) =>
        // Date Technical Debt. TO be removed once the Golang Fix is in Place
        val updatedPost = value.copy(year = link.datePublished.getYear,
          month = link.datePublished.getMonthValue, date = link.datePublished.getDayOfMonth, datePublished = link.datePublished)
        // Date Technical Debt. TO be removed once the Golang Fix is in Place
        Some(updatedPost)
      case Left(error) =>

        None
    }
  }


}
