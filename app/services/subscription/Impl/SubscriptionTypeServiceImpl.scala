package services.subscription.Impl

import domain.subscription.SubscriptionTypes
import repositories.subscription.SubscriptionTypesRepository
import services.subscription.SubscriptionTypeService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SubscriptionTypeServiceImpl extends SubscriptionTypeService with SubscriptionTypesRepository{

  override def save(subscriptionTypes: SubscriptionTypes): Future[Boolean] = {
    database.subscriptionTypesTable.save(subscriptionTypes) map (response => response.isExhausted())
  }

  override def getSubscription(id: String): Future[Option[SubscriptionTypes]] = {
    database.subscriptionTypesTable.getSubscription(id)
  }

  override def getSubscriptions: Future[Seq[SubscriptionTypes]] = {
    database.subscriptionTypesTable.getSubscriptions
  }
}
