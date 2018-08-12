package repositories.subscription

import com.outworkers.phantom.dsl._
import conf.connections.DataConnection
import repositories.subscription.tables.SubscriptionTableImpl

trait SubscriptionRepository  {
  def  database = SubscriptionDatabase
}

class SubscriptionDatabase (override val connector: KeySpaceDef) extends Database[SubscriptionDatabase](connector) {
  object subscriptionTable extends SubscriptionTableImpl with connector.Connector
}

object SubscriptionDatabase extends SubscriptionDatabase(DataConnection.connector)



