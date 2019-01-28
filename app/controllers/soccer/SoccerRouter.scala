package controllers.soccer

import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class SoccerRouter @Inject()(soccerController: SoccerController) extends SimpleRouter {
  override def routes: Routes = {
    // for teams
    case POST(p"/team/create") => soccerController.create
  }
}
