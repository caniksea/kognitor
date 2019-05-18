package services.actors

import akka.actor.{Actor, ActorLogging, Props}
import services.actors.LearnActor.Learn
import services.learning.LearningService

class LearnActor extends Actor with ActorLogging  {

  def processLearn(teams: Seq[String], target: String): Unit = {
    LearningService.apply.learnForAll(target)
//    teams.foreach(teamId => {
//      println("learning on team with id: ", teamId)
//      LearningService.apply.learn(teamId, target)
//    })
  }

  override def receive: Receive = {
    case learn: Learn => {
      println("Calling learnactor")
      processLearn(learn.teams, "rt")
    }
    case _ => println(s"${self.path} says Unknown message type!")
  }
}

object LearnActor {
  def props: Props = Props[LearnActor]

//  case class Learn(teams: Seq[String], target: String)

  case class Learn(teams: Seq[String])
}
