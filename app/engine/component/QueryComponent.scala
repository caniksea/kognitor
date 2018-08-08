package engine.component

import model.{QueryRequest, QueryResponse, ReasoningResponse}

object QueryComponent {
  def query(queryRequest: QueryRequest): QueryResponse = {
    val team = queryRequest.team
    val model = queryRequest.model
    var reasoningResponse: ReasoningResponse = null
    if (model.equals("RT")) reasoningResponse = RTReasoningComponent.reason(team)
    else reasoningResponse = BatchReasoningComponent.reason(team)
    QueryResponse(true, "Reasoning was successful", reasoningResponse)
  }
}
