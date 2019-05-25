package services.actors.common

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.routing.{DefaultResizer, RoundRobinPool}
import conf.connections.Configuration
import services.actors.common.LearnActor.Learn

class LearnActor extends Actor with ActorLogging  {

  val teamOneLearnActorProps: Props = TeamOneLearnActor.props

  val resizer: DefaultResizer =
    DefaultResizer(lowerBound = Configuration.config.getInt("custom-akka-actors.actorNumbers.lowerBound"),
      upperBound = Configuration.config.getInt("custom-akka-actors.actorNumbers.upperBound"))

  val teamOneLearnActorRef: ActorRef =
    context.actorOf(RoundRobinPool(Configuration.config.getInt("custom-akka-actors.actorNumbers.start"),
      Some(resizer)).props(teamOneLearnActorProps))

  def processLearn(teams: Seq[String], target: String): Unit = {
    val team1 = teams.head
    val restOfTeam = teams.tail
    val team2 = restOfTeam.head

    teamOneLearnActorRef ! TeamOneLearnActor.LearnOnTeam(team1, team2, target)
  }

  override def receive: Receive = {
    case learn: Learn => {
      processLearn(learn.teams, learn.target)
    }
    case _ => println(s"${self.path} says Unknown message type!")
  }
}

object LearnActor {
  def props: Props = Props[LearnActor]

  case class Learn(teams: Seq[String], target: String)
}
