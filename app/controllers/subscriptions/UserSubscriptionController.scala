package controllers.subscriptions

import domain.subscription.{Subscription, UserSubscriptions}
import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.subscription.UserSubscriptionService

import scala.concurrent.{ExecutionContext, Future}

class UserSubscriptionController @Inject()
(cc: ControllerComponents)
(implicit ec: ExecutionContext) extends AbstractController(cc){

  def update=Action.async(parse.json){
    request =>
      val input = request.body
      val entity = Json.fromJson[UserSubscriptions](input).get
      val response: Future[Boolean] = UserSubscriptionService.apply.save(entity)
      response.map(ans => Ok(Json.toJson(ans)))
        .recover {
          case otherException: Exception => InternalServerError
        }
  }

  def getUserSubscription(sitedId: String, userId: String, subscriptionId: String)=Action.async{
    implicit request: Request[AnyContent] =>
      val input = request.body
      val response: Future[Option[UserSubscriptions]] = for {
        results <- UserSubscriptionService.apply.getUserSubscription(sitedId,userId,subscriptionId)
      } yield results
      response.map(ans => Ok(Json.toJson(ans)))
        .recover {
          case otherException: Exception => InternalServerError
        }
  }

  def getUserSubscriptions(userId: String)=Action.async{
    implicit request: Request[AnyContent] =>
      val input = request.body
      val response: Future[Seq[UserSubscriptions]] = for {
        results <- UserSubscriptionService.apply.getUserSubscriptions(userId)
      } yield results
      response.map(ans => Ok(Json.toJson(ans)))
        .recover {
          case otherException: Exception => InternalServerError
        }
  }

  def getSiteSubscriptions(sitedId: String)=Action.async{
    implicit request: Request[AnyContent] =>
      val input = request.body
      val response: Future[Seq[UserSubscriptions]] = for {
        results <- UserSubscriptionService.apply.getSiteSubscriptions(sitedId)
      } yield results
      response.map(ans => Ok(Json.toJson(ans)))
        .recover {
          case otherException: Exception => InternalServerError
        }
  }

  def createUserSubscription(sitedId: String, userId: String, subscriptionId: String)=Action.async{
    implicit request: Request[AnyContent] =>
      val input = request.body
      val response: Future[Boolean] = for {
        results <- UserSubscriptionService.apply.createUserSubscription(sitedId,userId,subscriptionId)
      } yield results
      response.map(ans => Ok(Json.toJson(ans)))
        .recover {
          case otherException: Exception => InternalServerError
        }
  }

  def validateSubscription(userId: String)=Action.async{
    implicit request: Request[AnyContent] =>
      val input = request.body
      val response: Future[Boolean] = for {
        results <- UserSubscriptionService.apply.validateSubscription(userId)
      } yield results
      response.map(ans => Ok(Json.toJson(ans)))
        .recover {
          case otherException: Exception => InternalServerError
        }
  }

  def getUserSubscriptionType(userId:String)=Action.async{
    implicit request: Request[AnyContent] =>
      val input = request.body
      val response: Future[String] = for {
        results <- UserSubscriptionService.apply.getUserSubscriptionType(userId)
      } yield results
      response.map(ans => Ok(Json.toJson(ans)))
        .recover {
          case otherException: Exception => InternalServerError
        }
  }

  def getUserLatestSubscription(userId:String)=Action.async{
    implicit request: Request[AnyContent] =>
      val input = request.body
      val response: Future[Option[Subscription]] = for {
        results <- UserSubscriptionService.apply.getUserLatestSubscription(userId)
      } yield results
      response.map(ans => Ok(Json.toJson(ans)))
        .recover {
          case otherException: Exception => InternalServerError
        }
  }
}
