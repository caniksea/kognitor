package services.subscription.Impl

import java.time.LocalDateTime

import domain.subscription.{Subscription, UserSubscriptions}
import repositories.subscription.UserSubscriptionsRepository
import services.subscription.{SubscriptionService, UserSubscriptionService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserSubscriptionServiceImpl extends UserSubscriptionService with UserSubscriptionsRepository {


  override def save(userSubscriptions: UserSubscriptions): Future[Boolean] = {
    database.userSubscriptionsTable.save(userSubscriptions) map (result => result.isExhausted())
  }

  override def getUserSubscription(sitedId: String, userId: String, subscriptionId: String): Future[Option[UserSubscriptions]] = {
    database.userSubscriptionsTable.getUserSubscription(sitedId, userId, subscriptionId)
  }

  override def getUserSubscriptions(userId: String): Future[Seq[UserSubscriptions]] = {
    database.userBySubscriptionsTable.getUserSubscriptions(userId)
  }

  override def getSiteSubscriptions(sitedId: String): Future[Seq[UserSubscriptions]] = {
    database.userSubscriptionsTable.getSiteSubscriptions(sitedId)
  }

  override def createUserSubscription(sitedId: String, userId: String, subscriptionId: String): Future[Boolean] = {
    val result = SubscriptionService.apply.getSubscription(subscriptionId)
    val savedSubscription = result map {
      maybeSubscription: Option[Subscription] =>
        maybeSubscription match {
          case Some(value) => {
            val userSub = UserSubscriptions(sitedId,
              userId,
              subscriptionId,
              LocalDateTime.now,
              LocalDateTime.now.plusDays(value.duration))
            save(userSub) map (result => result)
          }
          case None => Future {
            false
          }
        }
    }
    savedSubscription.flatten
  }

  override def validateSubscription(userId: String): Future[Boolean] = {
    val userSubscriptions = UserSubscriptionService.apply.getUserSubscriptions(userId)
    userSubscriptions.map(subs =>
      if (subs.sorted(UserSubscriptions.orderByStartDate).head.endDate.isAfter(LocalDateTime.now)) true else false
    )
  }

  override def getUserSubscriptionType(userId: String): Future[String] = {
    for {
      validate <-validateSubscription(userId)
      userSubscriptions <- getUserLatestSubscription(userId) if validate
    } yield userSubscriptions.get.subscriptionType
  }

  override def getUserLatestSubscription(userId: String): Future[Option[Subscription]] = {
    val userSubscriptions = UserSubscriptionService.apply.getUserSubscriptions(userId)
    for {
      userSubscription <- userSubscriptions.map(subs => subs.sorted(UserSubscriptions.orderByStartDate).head)
      subscription <- SubscriptionService.apply.getSubscription(userSubscription.subscriptionId)
    } yield subscription
  }
}
