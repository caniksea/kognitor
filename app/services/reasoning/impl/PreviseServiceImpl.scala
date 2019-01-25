package services.reasoning.impl

import component.request.PreviseComponent
import domain.reasoning.{Previse, PreviseResult}
import services.reasoning.PreviseService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class PreviseServiceImpl extends PreviseService{
  override def previse(request: Previse): Future[PreviseResult] = {
    Future {
      PreviseComponent.previse(request)
    }
  }
}
