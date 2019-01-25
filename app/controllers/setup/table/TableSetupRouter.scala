package controllers.setup.table

import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class TableSetupRouter @Inject() (tableSetupController: TableSetupController) extends SimpleRouter {
  override def routes: Routes = {
    case POST(p"/tables/create") => tableSetupController.createTables
  }
}
