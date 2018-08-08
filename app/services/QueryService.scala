package services

import model.{QueryRequest, QueryResponse}
import services.impl.QueryServiceImpl

import scala.concurrent.Future

trait QueryService {
  def query(queryRequest: QueryRequest): Future[QueryResponse]
}

object QueryService{
  def apply: QueryService = new QueryServiceImpl()
}
