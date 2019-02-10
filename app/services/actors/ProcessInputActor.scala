package services.actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.routing.DefaultResizer


object ProcessInputActor{

  def props: Props = Props[ProcessInputActor]
}

class ProcessInputActor  extends Actor with ActorLogging{

  val resizer: DefaultResizer = DefaultResizer(lowerBound = Util.config.getInt("actorNumbers.lowerBound"), upperBound = Util.config.getInt("actorNumbers.upperBound"))

  override def receive: Receive = {


    case "START" => {
      


    }
    case _ => println(" This is Unknow Message ")
  }

}
