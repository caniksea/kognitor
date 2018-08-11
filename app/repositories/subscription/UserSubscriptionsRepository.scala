package repositories.subscription

import com.outworkers.phantom.dsl._
import conf.connections.DataConnection
import repositories.subscription.tables.{UserBySubscriptionTableImpl, UserSubscriptionsTableImpl}

trait UserSubscriptionsRepository  {
  def  database = UserSubscriptionsDatabase
}

class UserSubscriptionsDatabase (override val connector: KeySpaceDef) extends Database[UserSubscriptionsDatabase](connector) {
  object userSubscriptionsTable extends UserSubscriptionsTableImpl with connector.Connector
  object userBySubscriptionsTable extends UserBySubscriptionTableImpl with connector.Connector
}

object UserSubscriptionsDatabase extends UserSubscriptionsDatabase(DataConnection.connector)


