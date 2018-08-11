package domain.syslog

import java.time.LocalDateTime

import play.api.libs.json.Json


case class SystemLogEvents(siteId: String,
                           id: String,
                           eventName: String,
                           eventType: String,
                           message: String,
                           date: LocalDateTime)

object SystemLogEvents {
  implicit val syseventLog = Json.format[SystemLogEvents]

  def identity:SystemLogEvents = SystemLogEvents("","","","","",LocalDateTime.now)
}
