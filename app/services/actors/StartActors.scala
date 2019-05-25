package services.actors

import akka.actor.ActorSystem
import javax.inject.Inject


import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class StartActors @Inject()(system: ActorSystem)(implicit executionContext: ExecutionContext) {

  val feederActor = system.actorOf(FeederActor.props, "feeder-actor")

  system.scheduler.schedule(
    initialDelay = 0.microseconds,
    interval = 2.minutes,
    receiver = feederActor,
    message = "START"
  )

}
