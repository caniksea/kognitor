package controllers.subscriptions

import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class SubsRouter @Inject()
(subscriptionController: SubscriptionController,
 subscriptionTypeController: SubscriptionTypeController,
 userSubscriptionController: UserSubscriptionController) extends SimpleRouter{

  override def routes: Routes = {

    //Subscriptions

    case POST(p"/subscriptions/create") =>
      subscriptionController.createSubscriptions
    case POST(p"/subscriptions/update") =>
      subscriptionController.update
    case GET(p"/subscriptions/active") =>
      subscriptionController.getActiveSubscriptions
    case GET(p"/subscriptions/deactivated") =>
      subscriptionController.getDeactivatedSubscriptions
    case GET(p"/subscriptions/$subId") =>
      subscriptionController.getSubscription(subId)
    case GET(p"/subscriptions/get") =>
      subscriptionController.getSubscriptions

    // Subscription Types

    case POST(p"/types/create") =>
      subscriptionTypeController.create
    case GET(p"/types/get") =>
      subscriptionTypeController.getSubscriptions
    case GET(p"/types/$subId") =>
      subscriptionTypeController.getSubscription(subId)

    // User Subscriptions

    case GET(p"/user/create/$siteId/$userId/$subId") =>
      userSubscriptionController.createUserSubscription(siteId,userId,subId)
    case POST(p"/user/update") =>
      userSubscriptionController.update
    case GET(p"/user/site/$siteId") =>
      userSubscriptionController.getSiteSubscriptions(siteId)
    case POST(p"/user/latest/$userId") =>
      userSubscriptionController.getUserLatestSubscription(userId)
    case GET(p"/user/get/$userId") =>
      userSubscriptionController.getUserSubscriptions(userId)
    case GET(p"/user/type/$userId") =>
      userSubscriptionController.getUserSubscriptionType(userId)
    case GET(p"/user/validate/$userId") =>
      userSubscriptionController.validateSubscription(userId)
    case GET(p"/user/$siteId/$userId/$subId") =>
      userSubscriptionController.getUserSubscription(siteId,userId,subId)
  }

}
