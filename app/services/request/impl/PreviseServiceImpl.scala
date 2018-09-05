package services.request.impl

import component.request.PreviseComponent
import domain.request.Previse
import domain.response.PreviseResult
import services.request.PreviseService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class PreviseServiceImpl extends PreviseService{
  override def previse(request: Previse): Future[PreviseResult] = {
    Future {
      PreviseComponent.previse(request)
    }
  }
}
