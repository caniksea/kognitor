package services.actors

import akka.actor.ActorSystem
import javax.inject.Inject


import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class StartActors @Inject()(system: ActorSystem)(implicit executionContext: ExecutionContext) {

  val zoneActor = system.actorOf(ProcessInputActor.props, "zone-actor")

  system.scheduler.schedule(
    initialDelay = 0.microseconds,
    interval = 10.minutes,
    receiver = zoneActor,
    message = "START"
  )

}
