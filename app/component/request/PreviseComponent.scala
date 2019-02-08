package component.request

import component.reasoning.Reason
import domain.reasoning.{Previse, PreviseResult}

import scala.concurrent.Future

object PreviseComponent {

  def previse(request: Previse): Future[PreviseResult] = {
    Reason.processRequest(request)
  }
}
