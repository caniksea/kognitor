package controllers.setup.table

import javax.inject.Inject
import play.api.mvc._
import services.setup.table.CassandraSetupService

import scala.concurrent.ExecutionContext

class TableSetupController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {
  def createTables: Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      val tableCreation = CassandraSetupService.apply.createTables
      tableCreation.map(result => Ok("Tables created: " + result))
        .recover{
          case otherException: Exception => InternalServerError
        }
  }
}
