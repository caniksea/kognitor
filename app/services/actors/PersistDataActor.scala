package services.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.routing.{DefaultResizer, RoundRobinPool}
import conf.connections.Configuration
import domain.datainjection.Post
import domain.feeder.{FixtureFeeder, FormFeeder, RatingFeeder}
import services.actors.PersistDataActor.{PersistData, PersistMessage}
import services.feeder.DataInjectionService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object PersistDataActor {
  val props: Props = Props[PersistDataActor]

  case class PersistMessage(message: Post)

  case class PersistData(fixtures: Seq[FixtureFeeder], forms: Seq[FormFeeder], ratings: Seq[RatingFeeder])

}

class PersistDataActor extends Actor with ActorLogging {

  val learnActorProps = LearnActor.props

  val resizer: DefaultResizer =
    DefaultResizer(lowerBound = Configuration.config.getInt("custom-akka-actors.actorNumbers.lowerBound"),
      upperBound = Configuration.config.getInt("custom-akka-actors.actorNumbers.upperBound"))

  val learnActorRef: ActorRef =
  context.actorOf(RoundRobinPool(Configuration.config.getInt("custom-akka-actors.actorNumbers.start"),
    Some(resizer)).props(learnActorProps))

  def processSave(fixtures: Seq[FixtureFeeder], forms: Seq[FormFeeder], ratings: Seq[RatingFeeder]): Unit = {
//    DataInjectionService.apply.saveData(fixtures, forms, ratings)
//    println(s"Should call sender: $sender ")
//    sender ! FeederActor.DataFetch
    for {
      teams <- DataInjectionService.apply.saveData(fixtures, forms, ratings)
    } yield {
      println(s"Should call learn actor with: ", teams)
      learnActorRef ! LearnActor.Learn(teams)
    }
  }

  override def receive: PartialFunction[Any, Unit] = {
    case post: PersistMessage => {
      if (post.message.title.length != 0) {
        // Save Service Here
      }

    }
    case persistData: PersistData => processSave(persistData.fixtures, persistData.forms, persistData.ratings)

    case _ => println(s"${self.path} says Unknown message type!")

  }
}

