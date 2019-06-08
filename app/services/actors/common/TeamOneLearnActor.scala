package services.actors.common

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.routing.{DefaultResizer, RoundRobinPool}
import conf.connections.Configuration
import services.actors.common.TeamOneLearnActor.LearnOnTeam
import services.learning.LearningService

import scala.concurrent.ExecutionContext.Implicits.global

class TeamOneLearnActor extends Actor with ActorLogging  {

  val teamTwoLearnActorProps: Props = TeamTwoLearnActor.props

  val resizer: DefaultResizer =
    DefaultResizer(lowerBound = Configuration.config.getInt("custom-akka-actors.actorNumbers.lowerBound"),
      upperBound = Configuration.config.getInt("custom-akka-actors.actorNumbers.upperBound"))

  val teamTwoLearnActorRef: ActorRef =
    context.actorOf(RoundRobinPool(Configuration.config.getInt("custom-akka-actors.actorNumbers.start"),
      Some(resizer)).props(teamTwoLearnActorProps))

  def processLearn(learn: LearnOnTeam): Unit = {
    for {
      result <- LearningService.apply.learn(learn.teamOneId, learn.target)
    } yield {
      println(s"${self.path} response from learning: ${result}")
      teamTwoLearnActorRef ! TeamTwoLearnActor.LearnOnTeam(learn.teamTwoId, learn.target)
    }
  }

  override def receive: Receive = {
    case learn: LearnOnTeam => {
      processLearn(learn)
    }
    case _ => println(s"${self.path} says Unknown message type!")
  }
}

object TeamOneLearnActor {
  def props: Props = Props[TeamOneLearnActor]

  case class LearnOnTeam(teamOneId: String, teamTwoId: String, target: String)
}
