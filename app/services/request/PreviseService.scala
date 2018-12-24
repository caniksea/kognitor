package services.request

import domain.request.Previse
import domain.response.PreviseResult
import services.request.impl.PreviseServiceImpl

import scala.concurrent.Future

trait PreviseService {
  def previse(request: Previse): Future[PreviseResult]
}

object PreviseService {
  def apply: PreviseService = new PreviseServiceImpl()
}
