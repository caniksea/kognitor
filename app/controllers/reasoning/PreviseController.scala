package controllers.reasoning

import domain.reasoning.{Previse, PreviseResult}
import javax.inject.Inject
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AbstractController, Action, ControllerComponents}
import services.reasoning.PreviseService

import scala.concurrent.{ExecutionContext, Future}

class PreviseController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {
  def previse: Action[JsValue] = Action.async(parse.json) {
    request =>
      val input = request.body
      val entity = Json.fromJson[Previse](input).get
      val response: Future[PreviseResult] = PreviseService.apply.previse(entity)
      response.map(ans => Ok(Json.toJson(ans)))
        .recover{
          case exception: Exception => {
            println(exception.getMessage); exception.printStackTrace; InternalServerError
          }
        }
  }
}
