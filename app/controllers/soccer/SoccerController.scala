package controllers.soccer

import java.util.UUID

import domain.soccer.Team
import javax.inject.Inject
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.soccer.TeamService

import scala.concurrent.{ExecutionContext, Future}

class SoccerController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def processSave(team: Option[Team], entity: Team) = {
    println("option: ", team)
    println("concrete: ", entity)
    team match {
      case Some(value) => Future{Ok(Json.toJson(value))}
      case None => {
        val response = for {
          masterResult <- TeamService.masterImpl.saveEntity(entity)
          pseudomasterResult <- TeamService.pseudomasterImpl.saveEntity(entity)
        } yield masterResult && pseudomasterResult
        response.map(ans => Ok(Json.toJson(entity)))
          .recover {
            case otherException: Exception => InternalServerError
          }
      }
    }
  }

  def create: Action[JsValue] = Action.async(parse.json) {
    request =>
      val input = request.body
      val inputEntity = Json.fromJson[Team](input).get
      val entity = new Team(UUID.randomUUID().toString, inputEntity.teamName.trim)
      println(inputEntity, entity)

      val resp = for {
        team <- TeamService.masterImpl.findByName(entity.teamName)
        result <- processSave(team, entity)
      } yield result

      resp
  }

}
