package repositories.subscription.tables

import java.time.LocalDateTime

import com.outworkers.phantom.dsl._
import com.outworkers.phantom.jdk8._
import com.outworkers.phantom.streams._
import domain.subscription.Subscription

import scala.concurrent.Future


abstract class SubscriptionTable extends Table[SubscriptionTable, Subscription] {

  object subscriptionId extends StringColumn with PartitionKey

  object subscriptionType extends StringColumn

  object value extends BigDecimalColumn

  object duration extends IntColumn

  object dateCreated extends Col[LocalDateTime]

  object status extends StringColumn

}

abstract class SubscriptionTableImpl extends SubscriptionTable with RootConnector {

  override lazy val tableName = "subs"

  def save(subscription: Subscription): Future[ResultSet] = {
    insert
      .value(_.subscriptionId, subscription.subscriptionId)
      .value(_.subscriptionType, subscription.subscriptionType)
      .value(_.value, subscription.value)
      .value(_.duration, subscription.duration)
      .value(_.dateCreated, subscription.dateCreated)
      .value(_.status, subscription.status)
      .future()
  }

  def getSubscription(subscriptionId: String): Future[Option[Subscription]] = {
    select
      .where(_.subscriptionId eqs subscriptionId)
      .one()
  }

  def getSubscriptions:Future[Seq[Subscription]] ={
    select.fetchEnumerator() run Iteratee.collect()
  }

}
