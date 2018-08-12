package repositories.syslog

import com.outworkers.phantom.dsl._
import conf.connections.DataConnection
import repositories.syslog.tables.SystemLogEventsTableImpl



/**
  * Created by hashcode on 2017/01/29.
  */
class SystemLogEventsDatabase (override val connector: KeySpaceDef) extends Database[SystemLogEventsDatabase](connector) {

  object systemLogEventsTable extends SystemLogEventsTableImpl with connector.Connector
}

object SystemLogEventsDatabase extends SystemLogEventsDatabase(DataConnection.connector)

trait SyslogRepository  {

  def  database = SystemLogEventsDatabase
}



