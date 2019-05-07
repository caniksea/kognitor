package services.learning.impl

import component.learning.LearningComponent
import domain.learning.{LearningRequest, LearningResponse}
import services.learning.LearningService

import scala.concurrent.Future

class LearningServiceImpl extends LearningService {
  override def learn(teamId: String, target: String): Future[LearningResponse] =
    LearningComponent.learn(teamId, target)

  override def learnForAll(target: String): Future[Seq[LearningResponse]] = LearningComponent.learnForAll(target)
}
