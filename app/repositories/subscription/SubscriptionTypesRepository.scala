package repositories.subscription

import com.outworkers.phantom.dsl._
import conf.connections.DataConnection
import repositories.subscription.tables.SubscriptionTypesTableImpl

trait SubscriptionTypesRepository {
  def database = SubscriptionTypesDatabase
}

class SubscriptionTypesDatabase(override val connector: KeySpaceDef) extends Database[SubscriptionTypesDatabase](connector) {

  object subscriptionTypesTable extends SubscriptionTypesTableImpl with connector.Connector

}

object SubscriptionTypesDatabase extends SubscriptionTypesDatabase(DataConnection.connector)



