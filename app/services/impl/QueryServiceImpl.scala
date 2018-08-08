package services.impl

import controllers.MainController
import model.{QueryRequest, QueryResponse}
import services.QueryService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class QueryServiceImpl extends QueryService{
  override def query(queryRequest: QueryRequest): Future[QueryResponse] = {
    Future {
      MainController.query(queryRequest)
    }
  }
}
