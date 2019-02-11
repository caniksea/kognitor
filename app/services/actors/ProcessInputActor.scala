package services.actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.routing.DefaultResizer
import conf.connections.Configuration


object ProcessInputActor{
  def props: Props = Props[ProcessInputActor]
}

class ProcessInputActor  extends Actor with ActorLogging{

  val resizer: DefaultResizer =
    DefaultResizer(lowerBound = Configuration.config.getInt("custom-akka-actors.actorNumbers.lowerBound"),
      upperBound = Configuration.config.getInt("custom-akka-actors.actorNumbers.upperBound"))

  override def receive: Receive = {
    case "START" => {}
    case _ => println(" This is Unknown Message ")
  }

}
