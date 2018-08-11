package repositories.subscription.tables

import java.time.LocalDateTime

import com.outworkers.phantom.dsl._
import com.outworkers.phantom.jdk8._
import com.outworkers.phantom.streams._
import domain.subscription.UserSubscriptions

import scala.concurrent.Future

abstract class UserSubscriptionsTable extends Table[UserSubscriptionsTable, UserSubscriptions] {

  object siteId extends StringColumn with PartitionKey

  object userId extends StringColumn with PrimaryKey

  object subscriptionId extends StringColumn with PrimaryKey

  object startDate extends Col[LocalDateTime]

  object endDate extends Col[LocalDateTime]

}

abstract class  UserSubscriptionsTableImpl extends UserSubscriptionsTable with RootConnector {

  override lazy val tableName = "usersubs"

  def save(userSubscriptions: UserSubscriptions): Future[ResultSet] = {
    insert
      .value(_.siteId, userSubscriptions.siteId)
      .value(_.userId, userSubscriptions.userId)
      .value(_.subscriptionId, userSubscriptions.subscriptionId)
      .value(_.startDate, userSubscriptions.startDate)
      .value(_.endDate, userSubscriptions.endDate)
      .future()
  }

  def getUserSubscription(sitedId: String, userId:String,subscriptionId:String): Future[Option[UserSubscriptions]] = {
    select
      .where(_.siteId eqs sitedId)
      .and(_.userId eqs userId)
      .and(_.subscriptionId eqs subscriptionId)
      .one()
  }


  def getSiteSubscriptions(sitedId: String): Future[Seq[UserSubscriptions]] = {
    select
      .where(_.siteId eqs sitedId)
      .fetchEnumerator() run Iteratee.collect()
  }

}

abstract class UserBySubscriptionsTable extends Table[UserBySubscriptionsTable, UserSubscriptions] {

  object siteId extends StringColumn with PrimaryKey

  object userId extends StringColumn with PartitionKey

  object subscriptionId extends StringColumn with PrimaryKey

  object startDate extends Col[LocalDateTime]

  object endDate extends Col[LocalDateTime]

}

abstract class  UserBySubscriptionTableImpl extends UserBySubscriptionsTable with RootConnector {

  override lazy val tableName = "userbysubs"

  def save(userSubscriptions: UserSubscriptions): Future[ResultSet] = {
    insert
      .value(_.siteId, userSubscriptions.siteId)
      .value(_.userId, userSubscriptions.userId)
      .value(_.subscriptionId, userSubscriptions.subscriptionId)
      .value(_.startDate, userSubscriptions.startDate)
      .value(_.endDate, userSubscriptions.endDate)
      .future()
  }


  def getUserSubscriptions(userId:String): Future[Seq[UserSubscriptions]] = {
    select
      .where(_.userId eqs userId)
      .fetchEnumerator() run Iteratee.collect()
  }


}

