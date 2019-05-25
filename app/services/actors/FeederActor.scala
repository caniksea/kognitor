package services.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.routing.{DefaultResizer, RoundRobinPool}
import conf.connections.Configuration
import services.actors.batch.BatchLearnActor
import services.feeder.DatasourceService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


object FeederActor {
  def props: Props = Props[FeederActor]
}

class FeederActor extends Actor with ActorLogging {

  val persistDataActorProps: Props = PersistDataActor.props

  val batchLearnActorProps: Props = BatchLearnActor.props

  val resizer: DefaultResizer =
    DefaultResizer(lowerBound = Configuration.config.getInt("custom-akka-actors.actorNumbers.lowerBound"),
      upperBound = Configuration.config.getInt("custom-akka-actors.actorNumbers.upperBound"))
  val persistDataActorActorRef: ActorRef =
    context.actorOf(RoundRobinPool(Configuration.config.getInt("custom-akka-actors.actorNumbers.start"),
      Some(resizer)).props(persistDataActorProps))
  val batchLearnActorRef: ActorRef =
    context.actorOf(RoundRobinPool(Configuration.config.getInt("custom-akka-actors.actorNumbers.start"),
      Some(resizer)).props(batchLearnActorProps))

  context.system.scheduler.schedule(
    initialDelay = 1.minutes,
    interval = 5.minutes,
    receiver = batchLearnActorRef,
    message = "batch-learn"
  )


  def getData = {

    for {
      (fixtureData, formData, ratingData) <- DatasourceService.apply.getData
    } yield {
      persistDataActorActorRef ! PersistDataActor.PersistData(fixtureData, formData, ratingData)
    }
  }

  override def receive: PartialFunction[Any, Unit] = {
    case "START" => getData
    case _ => println(" This is Unknown Message ")
  }

}
