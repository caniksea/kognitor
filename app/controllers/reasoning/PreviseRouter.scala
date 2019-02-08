package controllers.reasoning

import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class PreviseRouter @Inject() (previseController: PreviseController) extends SimpleRouter{
  override def routes: Routes = {
    case POST(p"/previse") =>
      previseController.previse
  }
}
