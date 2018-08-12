package repositories.syslog.tables

import java.time.LocalDateTime

import com.outworkers.phantom.dsl._
import com.outworkers.phantom.jdk8._
import com.outworkers.phantom.streams._
import domain.syslog.SystemLogEvents

import scala.concurrent.Future



abstract class SystemLogEventsTable extends Table[SystemLogEventsTable, SystemLogEvents] {

  object siteId extends StringColumn with PartitionKey

  object id extends StringColumn with PrimaryKey

  object eventName extends StringColumn

  object eventType extends StringColumn

  object message extends StringColumn

  object date extends Col[LocalDateTime]

}

abstract class SystemLogEventsTableImpl extends SystemLogEventsTable with RootConnector {

  override lazy val tableName = "systemLogEvents"

  def save(systemLogEvents: SystemLogEvents): Future[ResultSet] = {
    insert
      .value(_.siteId, systemLogEvents.siteId)
      .value(_.id, systemLogEvents.id)
      .value(_.eventName, systemLogEvents.eventName)
      .value(_.eventType, systemLogEvents.eventType)
      .value(_.message, systemLogEvents.message)
      .value(_.date, systemLogEvents.date)
      .ttl(5000)
      .future()

  }

  def getEventById(siteId: String, id: String): Future[Option[SystemLogEvents]] = {
    select
      .where(_.siteId eqs siteId)
      .and(_.id eqs id)
      .one()
  }

  def getSiteLogs(siteId: String): Future[Seq[SystemLogEvents]] = {
    select
      .where(_.siteId eqs siteId)
      .fetchEnumerator() run Iteratee.collect()
  }

}
