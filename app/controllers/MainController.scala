package controllers

import engine.component.QueryComponent
import model.{QueryRequest, QueryResponse}

object MainController {
  def query(queryRequest: QueryRequest): QueryResponse = {
    QueryComponent.query(queryRequest)
  }
}
