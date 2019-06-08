package services.actors.common

import akka.actor.{Actor, ActorLogging, Props}
import services.actors.common.TeamTwoLearnActor.LearnOnTeam
import services.learning.LearningService

import scala.concurrent.ExecutionContext.Implicits.global

class TeamTwoLearnActor extends Actor with ActorLogging  {

  def processLearn(teamId: String, target: String) = {
    for {
      result <- LearningService.apply.learn(teamId, target)
    } yield {
      println(s"${self.path} response from learning: ${result}")
    }
  }

  override def receive: Receive = {
    case learn: LearnOnTeam => {
      processLearn(learn.teamId, learn.target)
    }
    case _ => println(s"${self.path} says Unknown message type!")
  }
}

object TeamTwoLearnActor {
  def props: Props = Props[TeamTwoLearnActor]

  case class LearnOnTeam(teamId: String, target: String)
}
