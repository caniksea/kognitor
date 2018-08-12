package services.subscription

import domain.subscription.{Subscription, UserSubscriptions}
import services.subscription.Impl.UserSubscriptionServiceImpl

import scala.concurrent.Future

trait UserSubscriptionService {

  def save(userSubscriptions: UserSubscriptions): Future[Boolean]

  def getUserSubscription(sitedId: String, userId: String, subscriptionId: String): Future[Option[UserSubscriptions]]

  def getUserSubscriptions(userId: String): Future[Seq[UserSubscriptions]]

  def getSiteSubscriptions(sitedId: String): Future[Seq[UserSubscriptions]]

  def createUserSubscription(sitedId: String, userId: String, subscriptionId: String): Future[Boolean]

  def validateSubscription(userId: String): Future[Boolean]

  def getUserSubscriptionType(userId:String):Future[String]

  def getUserLatestSubscription(userId:String):Future[Option[Subscription]]

}

object UserSubscriptionService {
  def apply: UserSubscriptionService = new UserSubscriptionServiceImpl()
}
