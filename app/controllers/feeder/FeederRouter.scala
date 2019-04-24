package controllers.feeder

import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class FeederRouter @Inject() (feederController: FeederController) extends SimpleRouter {
  override def routes: Routes = {
    case POST(p"/add/$entity") =>
      feederController.add(entity)
  }
}
