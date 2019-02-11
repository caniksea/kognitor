package domain.feeder

import play.api.libs.json.Json

case class AppDatasource(event: String, source: String)

object AppDatasource {
  implicit val appDatasourceFormat = Json.format[AppDatasource]
}