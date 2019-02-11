package services.actors

import akka.actor.{Actor, ActorLogging, Props}
import domain.datainjection.Post
import services.actors.PersistDataActor.PersistMessage

object PersistDataActor {
  val props: Props = Props[PersistDataActor]

  case class PersistMessage(message: Post)

}

class PersistDataActor extends Actor with ActorLogging {
  override def receive: PartialFunction[Any, Unit] = {
    case post: PersistMessage => {
      if (post.message.title.length != 0) {
        // Save Service Here
      }

    }

  }
}

