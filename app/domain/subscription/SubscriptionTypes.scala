package domain.subscription

import play.api.libs.json.Json

case class SubscriptionTypes(id: String, name: String)

object SubscriptionTypes {

  implicit val subTypeFormat = Json.format[SubscriptionTypes]

  def identity: SubscriptionTypes = SubscriptionTypes("", "")
}
