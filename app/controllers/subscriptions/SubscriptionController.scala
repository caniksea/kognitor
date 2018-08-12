package controllers.subscriptions

import domain.subscription.Subscription
import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc._
import services.subscription.SubscriptionService

import scala.concurrent.{ExecutionContext, Future}

class SubscriptionController @Inject()
(cc: ControllerComponents)
(implicit ec: ExecutionContext) extends AbstractController(cc){

  def update= Action.async(parse.json){
    request =>
      val input = request.body
      val entity = Json.fromJson[Subscription](input).get
      val response: Future[Boolean] = SubscriptionService.apply.save(entity)
      response.map(ans => Ok(Json.toJson(ans)))
        .recover {
          case otherException: Exception => InternalServerError
        }
  }

  def createSubscriptions=Action.async(parse.json){

    request =>
      val input = request.body
      val entity = Json.fromJson[Subscription](input).get
      val response: Future[Boolean] = SubscriptionService.apply.createSubscriptions(entity)
      response.map(ans => Ok(Json.toJson(ans)))
        .recover {
          case otherException: Exception => InternalServerError
        }
  }

  def getSubscription(subscriptionId: String)=Action.async{
    implicit request: Request[AnyContent] =>
      val input = request.body
      val response: Future[Option[Subscription]] = for {
        results <- SubscriptionService.apply.getSubscription(subscriptionId)
      } yield results
      response.map(ans => Ok(Json.toJson(ans)))
        .recover {
          case otherException: Exception => InternalServerError
        }
  }

  def getSubscriptions=Action.async{
    implicit request: Request[AnyContent] =>
      val input = request.body
      val response: Future[Seq[Subscription]] = for {
        results <- SubscriptionService.apply.getSubscriptions
      } yield results
      response.map(ans => Ok(Json.toJson(ans)))
        .recover {
          case otherException: Exception => InternalServerError
        }
  }

  def getActiveSubscriptions=Action.async{
    implicit request: Request[AnyContent] =>
      val input = request.body
      val response: Future[Seq[Subscription]] = for {
        results <- SubscriptionService.apply.getActiveSubscriptions
      } yield results
      response.map(ans => Ok(Json.toJson(ans)))
        .recover {
          case otherException: Exception => InternalServerError
        }
  }

  def getDeactivatedSubscriptions=Action.async{
    implicit request: Request[AnyContent] =>
      val input = request.body
      val response: Future[Seq[Subscription]] = for {
        results <- SubscriptionService.apply.getDeactivatedSubscriptions
      } yield results
      response.map(ans => Ok(Json.toJson(ans)))
        .recover {
          case otherException: Exception => InternalServerError
        }
  }

}
