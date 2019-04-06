package services.actors

import java.time.LocalDateTime
import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.routing.{DefaultResizer, RoundRobinPool}
import conf.connections.Configuration
import domain.datainjection.Link
import services.actors.ProcessLinksActor.LinkMessage

object ProcessLinksActor {
  val props: Props = Props[ProcessLinksActor]

  case class LinkMessage(links: Seq[Link])


}

class ProcessLinksActor extends Actor with ActorLogging {

  val persistDataActorProps: Props = PersistDataActor.props

  val resizer =
    DefaultResizer(lowerBound = Configuration.config.getInt("custom-akka-actors.actorNumbers.lowerBound"),
      upperBound = Configuration.config.getInt("custom-akka-actors.actorNumbers.upperBound"))
  val persistDataActorActorRef: ActorRef =
    context.actorOf(RoundRobinPool(Configuration.config.getInt("custom-akka-actors.actorNumbers.start"),
      Some(resizer)).props(persistDataActorProps))


  override def receive: PartialFunction[Any, Unit] = {
//    case links: LinkMessage =>
//      links.links foreach { link =>
//        val article = /// Some Action Service
//        article map {
//          case Some(data) => persistDataActorProps ! PersistDataActor.PersistMessage(data)
//          case None => {
//           // Log EVEnt or Whatever
//          }
//        }
//      }
    null
  }
}

