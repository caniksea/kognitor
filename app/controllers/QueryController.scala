package controllers

import javax.inject.{Inject, Singleton}
import model.QueryRequest
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AbstractController, Action, ControllerComponents}
import services.QueryService

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class QueryController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def query: Action[JsValue] = Action.async(parse.json) {
    request =>
      val queryRequest = Json.fromJson[QueryRequest](request.body).get
      val queryResponse = QueryService.apply.query(queryRequest)
      queryResponse.map(ans => Ok(Json.toJson(ans)))
  }

}
