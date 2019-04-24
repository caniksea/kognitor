package controllers.feeder

import domain.feeder.{FixtureFeeder, FormFeeder, RatingFeeder}
import javax.inject.Inject
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AbstractController, Action, ControllerComponents, Result}
import services.feeder.{FixtureFeederService, FormFeederService, RatingFeederService}

import scala.concurrent.{ExecutionContext, Future}

class FeederController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc)  {

  def saveFixture(input: JsValue): Future[Result] = {
    val entity = Json.fromJson[FixtureFeeder](input).get
    val response = for {
      result <- FixtureFeederService.apply.saveEntity(entity)
    } yield result
    response.map(ans => Ok(Json.toJson(ans)))
      .recover{
        case exception: Exception => InternalServerError
      }
  }

  def saveForm(input: JsValue): Future[Result] = {
    val entity = Json.fromJson[FormFeeder](input).get
    println("saving form feeder:: ", entity)
    val response = for {
      result <- FormFeederService.apply.saveEntity(entity)
    } yield result
    response.map(ans => Ok(Json.toJson(ans)))
      .recover{
        case exception: Exception => InternalServerError
      }
  }

  def saveRating(input: JsValue): Future[Result] = {
    val entity = Json.fromJson[RatingFeeder](input).get
    println("saving rating feeder:: ", entity)
    val response = for {
      result <- RatingFeederService.apply.saveEntity(entity)
    } yield result
    response.map(ans => Ok(Json.toJson(ans)))
      .recover{
        case exception: Exception => InternalServerError
      }
  }

  def add(entity: String): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      var input = request.body
      entity.toLowerCase match {
        case "fixture" => saveFixture(input)
        case "form" => saveForm(input)
        case "rating" => saveRating(input)
        case _ => null
      }
  }

}
