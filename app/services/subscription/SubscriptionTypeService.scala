package services.subscription

import domain.subscription.SubscriptionTypes
import services.subscription.Impl.SubscriptionTypeServiceImpl

import scala.concurrent.Future

trait SubscriptionTypeService {

  def save(subscriptionTypes: SubscriptionTypes): Future[Boolean]

  def getSubscription(id: String): Future[Option[SubscriptionTypes]]

  def getSubscriptions:Future[Seq[SubscriptionTypes]]

}

object SubscriptionTypeService{
  def apply: SubscriptionTypeService = new SubscriptionTypeServiceImpl()
}
