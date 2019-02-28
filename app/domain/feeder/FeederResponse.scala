package domain.feeder

import play.api.libs.json.Json

case class FeederResponse (
                          feederData: Seq[FeederData]
                          )

object FeederResponse {
  implicit val feederResponseFormat = Json.format[FeederResponse]
}
