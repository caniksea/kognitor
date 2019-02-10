package services.datainjection

import domain.datainjection.{Link, Post}
import play.api.libs.json.{JsPath, JsonValidationError}
import services.datainjection.Impl.PageServiceImpl

import scala.concurrent.Future

trait PageService {
  type CrawlerError = Seq[(JsPath, Seq[JsonValidationError])]
  def getPost(link: Link):Future[Option[Post]]
}

object PageService{
  def apply: PageService = new PageServiceImpl()
}
