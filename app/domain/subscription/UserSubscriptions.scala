package domain.subscription

import java.time.LocalDateTime

import play.api.libs.json.Json

case class UserSubscriptions( siteId:String,
                              userId: String,
                              subscriptionId: String,
                              startDate: LocalDateTime,
                              endDate: LocalDateTime
                            )

object UserSubscriptions {
  implicit val subFomat = Json.format[UserSubscriptions]

  def identity: UserSubscriptions = UserSubscriptions("","", "",  LocalDateTime.now(), LocalDateTime.now())

  implicit object localDateTime extends Ordering[LocalDateTime] {
    def compare(x: LocalDateTime, y: LocalDateTime): Int = x.compareTo(y)
  }

   val orderByStartDate: Ordering[UserSubscriptions] = Ordering.by(subscription => subscription.startDate)
}
