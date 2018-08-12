package services.syslog

import com.outworkers.phantom.dsl.ResultSet
import domain.syslog.SystemLogEvents
import repositories.syslog.SyslogRepository

import scala.concurrent.Future


trait SyslogService extends SyslogRepository {

  def save(systemLogEvents: SystemLogEvents): Future[ResultSet] = {
    database.systemLogEventsTable.save(systemLogEvents)
  }

  def getEventById(siteId: String, id: String): Future[Option[SystemLogEvents]] = {
    database.systemLogEventsTable.getEventById(siteId, id)
  }

  def getSiteLogs(siteId: String): Future[Seq[SystemLogEvents]] = {
    database.systemLogEventsTable.getSiteLogs(siteId)
  }

}


object SyslogService extends SyslogService with SyslogRepository
