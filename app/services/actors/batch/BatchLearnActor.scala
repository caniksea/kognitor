package services.actors.batch

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.routing.{DefaultResizer, RoundRobinPool}
import conf.connections.Configuration
import services.actors.common.LearnActor
import services.soccer.TeamService

import scala.concurrent.ExecutionContext.Implicits.global

class BatchLearnActor extends Actor with ActorLogging  {

  val learnActorProps: Props = LearnActor.props

  val resizer: DefaultResizer =
    DefaultResizer(lowerBound = Configuration.config.getInt("custom-akka-actors.actorNumbers.lowerBound"),
      upperBound = Configuration.config.getInt("custom-akka-actors.actorNumbers.upperBound"))

  val learnActorRef: ActorRef =
    context.actorOf(RoundRobinPool(Configuration.config.getInt("custom-akka-actors.actorNumbers.start"),
      Some(resizer)).props(learnActorProps))


  def startBatchLearn(): Unit = {
    for {
      teams <- TeamService.masterImpl.getEntities
    } yield {
      val teamIds = teams.map(team => team.teamId)
      learnActorRef ! LearnActor.Learn(teamIds, "bt")
    }
  }

  override def receive: Receive = {
    case "batch-learn" =>
      println(s"${self} should start batch learn...")
      startBatchLearn()
    case _ => println(s"${self} says unknown message!")
  }
}

object BatchLearnActor {
  def props: Props = Props[BatchLearnActor]
}
