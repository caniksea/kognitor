package controllers.learning

import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class LearningRouter @Inject() (learningController: LearningController) extends SimpleRouter {
  override def routes: Routes = {
    case GET(p"/learn/$teamId/$target") => learningController.learn(teamId, target)
    case GET(p"/all/$target") => learningController.learnForAll(target)
  }
}
