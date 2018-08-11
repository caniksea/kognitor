package domain.subscription

import java.time.LocalDateTime

import play.api.libs.json.Json


case class Subscription( subscriptionId: String,
                         subscriptionType: String,
                         value: BigDecimal,
                         duration: Int,
                         description: String,
                         dateCreated: LocalDateTime,
                         status: String)

object Subscription {
  implicit val subFomat = Json.format[Subscription]

  def identity: Subscription = Subscription("", "", BigDecimal(0), 0,"", LocalDateTime.now, "")


}