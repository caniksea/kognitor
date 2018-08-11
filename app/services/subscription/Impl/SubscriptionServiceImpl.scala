package services.subscription.Impl

import conf.util.SubscriptionEvents
import domain.subscription.Subscription
import repositories.subscription.SubscriptionRepository
import services.subscription.SubscriptionService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SubscriptionServiceImpl extends SubscriptionService with SubscriptionRepository {

  override def save(subscription: Subscription): Future[Boolean] = {
    database.subscriptionTable.save(subscription) map (res => res.isExhausted())
  }

  override def getSubscription(subscriptionId: String): Future[Option[Subscription]] = {
    database.subscriptionTable.getSubscription(subscriptionId)
  }

  override def getSubscriptions: Future[Seq[Subscription]] = {
    database.subscriptionTable.getSubscriptions
  }

  override def createSubscriptions(subscription: Subscription): Future[Boolean] = {
    save(subscription.copy(status = SubscriptionEvents.ACTIVATED))
  }

  override def getActiveSubscriptions: Future[Seq[Subscription]] = {
    for {
      sub <- getActiveSubscriptions
    } yield sub.filter(subscription => subscription.status==SubscriptionEvents.ACTIVATED)
  }

  override def getDeactivatedSubscriptions: Future[Seq[Subscription]] = {
    for {
      sub <- getActiveSubscriptions
    } yield sub.filter(subscription => subscription.status==SubscriptionEvents.DEACTIVATED)

  }
}
