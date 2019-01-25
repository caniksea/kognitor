package services.learning

import domain.learning.{LearningRequest, LearningResponse}
import services.learning.impl.LearningServiceImpl

import scala.concurrent.Future

trait LearningService {
  def Learn(teamId: String): Future[LearningResponse]
}

object LearningService {
  def apply: LearningService = new LearningServiceImpl()
}
