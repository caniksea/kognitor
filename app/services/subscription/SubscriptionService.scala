package services.subscription

import domain.subscription.Subscription
import services.subscription.Impl.SubscriptionServiceImpl

import scala.concurrent.Future

trait SubscriptionService {

  def save(subscription: Subscription): Future[Boolean]

  def createSubscriptions(subscription: Subscription): Future[Boolean]

  def getSubscription(subscriptionId: String): Future[Option[Subscription]]

  def getSubscriptions: Future[Seq[Subscription]]

  def getActiveSubscriptions:Future[Seq[Subscription]]

  def getDeactivatedSubscriptions: Future[Seq[Subscription]]
}

object SubscriptionService{
  def apply: SubscriptionService = new SubscriptionServiceImpl()
}
