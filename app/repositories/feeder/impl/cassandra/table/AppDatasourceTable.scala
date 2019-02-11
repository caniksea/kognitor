package repositories.feeder.impl.cassandra.table

import com.outworkers.phantom.Table
import com.outworkers.phantom.connectors.RootConnector
import com.outworkers.phantom.keys.PartitionKey
import com.outworkers.phantom.dsl._
import com.outworkers.phantom.streams._
import domain.feeder.AppDatasource

import scala.concurrent.Future

abstract class AppDatasourceTable extends Table[AppDatasourceTable, AppDatasource] with RootConnector {

  override lazy val tableName = "appdatasource"

  object event extends StringColumn with PartitionKey

  object source extends StringColumn

  def getEventSources(event: String): Future[Seq[AppDatasource]] = {
    select
      .where(_.event eqs event)
      .fetchEnumerator() run Iteratee.collect()
  }

}
