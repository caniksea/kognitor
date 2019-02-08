package controllers.learning

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc._
import services.learning.LearningService

import scala.concurrent.ExecutionContext.Implicits.global

class LearningController @Inject()(cc: ControllerComponents) extends AbstractController(cc)  {

  def learn(teamId: String): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      val input = request.body
      val response = for {
        result <- LearningService.apply.Learn(teamId)
      } yield result
      response.map(ans => Ok(Json.toJson(ans)))
        .recover{
          case exception: Exception => {println(exception.getMessage); exception.printStackTrace; InternalServerError}
        }
  }

}
