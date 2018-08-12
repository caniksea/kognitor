package repositories.subscription.tables

import com.outworkers.phantom.dsl._
import com.outworkers.phantom.streams._
import domain.subscription.SubscriptionTypes

import scala.concurrent.Future

abstract class SubscriptionTypesTable extends Table[SubscriptionTypesTable, SubscriptionTypes] {

  object id extends StringColumn with PartitionKey

  object name extends StringColumn

}

abstract class  SubscriptionTypesTableImpl extends SubscriptionTypesTable with RootConnector {

  override lazy val tableName = "subtypes"

  def save(subscriptionTypes: SubscriptionTypes): Future[ResultSet] = {
    insert
      .value(_.id, subscriptionTypes.id)
      .value(_.name, subscriptionTypes.name)
      .future()
  }

  def getSubscription(id: String): Future[Option[SubscriptionTypes]] = {
    select
      .where(_.id eqs id)
      .one()
  }

  def getSubscriptions:Future[Seq[SubscriptionTypes]] ={
    select.fetchEnumerator() run Iteratee.collect()
  }


}
