package controllers.subscriptions

import domain.subscription.SubscriptionTypes
import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.subscription.SubscriptionTypeService

import scala.concurrent.{ExecutionContext, Future}

class SubscriptionTypeController @Inject()
(cc: ControllerComponents)
(implicit ec: ExecutionContext) extends AbstractController(cc){

  def create=Action.async(parse.json){
    request =>
      val input = request.body
      val entity = Json.fromJson[SubscriptionTypes](input).get
      val response: Future[Boolean] = SubscriptionTypeService.apply.save(entity)
      response.map(ans => Ok(Json.toJson(ans)))
        .recover {
          case otherException: Exception => InternalServerError
        }
  }

  def getSubscription(id: String)=Action.async{
    implicit request: Request[AnyContent] =>
      val input = request.body
      val response: Future[Option[SubscriptionTypes]] = for {
        results <- SubscriptionTypeService.apply.getSubscription(id)
      } yield results
      response.map(ans => Ok(Json.toJson(ans)))
        .recover {
          case otherException: Exception => InternalServerError
        }

  }

  def getSubscriptions=Action.async{
    implicit request: Request[AnyContent] =>
      val input = request.body
      val response: Future[Seq[SubscriptionTypes]] = for {
        results <- SubscriptionTypeService.apply.getSubscriptions
      } yield results
      response.map(ans => Ok(Json.toJson(ans)))
        .recover {
          case otherException: Exception => InternalServerError
        }

  }

}
