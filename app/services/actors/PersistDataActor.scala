package services.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.routing.{DefaultResizer, RoundRobinPool}
import conf.connections.Configuration
import domain.datainjection.Post
import domain.feeder.{FixtureFeeder, FormFeeder, RatingFeeder}
import services.actors.PersistDataActor.{PersistData, PersistMessage}
import services.actors.common.LearnActor
import services.feeder.DataInjectionService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object PersistDataActor {
  val props: Props = Props[PersistDataActor]

  case class PersistMessage(message: Post)

  case class PersistData(fixtures: Seq[FixtureFeeder], forms: Seq[FormFeeder], ratings: Seq[RatingFeeder])

}

class PersistDataActor extends Actor with ActorLogging {

  val learnActorProps: Props = LearnActor.props

  val resizer: DefaultResizer =
    DefaultResizer(lowerBound = Configuration.config.getInt("custom-akka-actors.actorNumbers.lowerBound"),
      upperBound = Configuration.config.getInt("custom-akka-actors.actorNumbers.upperBound"))

  val learnActorRef: ActorRef =
  context.actorOf(RoundRobinPool(Configuration.config.getInt("custom-akka-actors.actorNumbers.start"),
    Some(resizer)).props(learnActorProps))

  def processSave(fixtures: Seq[FixtureFeeder], forms: Seq[FormFeeder], ratings: Seq[RatingFeeder]): Unit = {
    for {
      teams <- DataInjectionService.apply.saveData(fixtures, forms, ratings)
    } yield {
      learnActorRef ! LearnActor.Learn(teams, "rt")
    }
  }

  override def receive: PartialFunction[Any, Unit] = {

    case persistData: PersistData => processSave(persistData.fixtures, persistData.forms, persistData.ratings)

    case _ => println(s"${self.path} says Unknown message type!")

  }
}

