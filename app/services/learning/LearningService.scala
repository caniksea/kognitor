package services.learning

import domain.learning.{LearningRequest, LearningResponse}
import services.learning.impl.LearningServiceImpl

import scala.concurrent.Future

trait LearningService {
  def learn(teamId: String): Future[LearningResponse]
  def learnForAll: Future[Seq[LearningResponse]]
}

object LearningService {
  def apply: LearningService = new LearningServiceImpl()
}
