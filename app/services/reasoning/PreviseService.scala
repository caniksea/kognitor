package services.reasoning

import domain.reasoning.{Previse, PreviseResult}
import services.reasoning.impl.PreviseServiceImpl

import scala.concurrent.Future

trait PreviseService {
  def previse(request: Previse): Future[PreviseResult]
}

object PreviseService {
  def apply: PreviseService = new PreviseServiceImpl()
}
