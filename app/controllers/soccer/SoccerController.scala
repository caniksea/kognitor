package controllers.soccer

import java.util.UUID

import domain.soccer.Team
import javax.inject.Inject
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.soccer.TeamService

import scala.concurrent.ExecutionContext

class SoccerController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {
  def create: Action[JsValue] = Action.async(parse.json) {
    request =>
      val input = request.body
      val entity = Json.fromJson[Team](input).get
      println(input)
      val team = new Team(UUID.randomUUID().toString, entity.teamName)
      println(entity, team)
      val response = for {
        masterResult <- TeamService.masterImpl.saveEntity(team)
        pseudomasterResult <- TeamService.pseudomasterImpl.saveEntity(team)
      } yield masterResult && pseudomasterResult
      response.map(ans => Ok(Json.toJson(team)))
        .recover {
          case otherException: Exception => InternalServerError
        }
  }

}
