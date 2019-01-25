package services.learning.impl

import component.learning.LearningComponent
import domain.learning.{LearningRequest, LearningResponse}
import services.learning.LearningService

import scala.concurrent.Future

class LearningServiceImpl extends LearningService {
  override def Learn(teamId: String): Future[LearningResponse] =
    LearningComponent.learn(teamId)
}
