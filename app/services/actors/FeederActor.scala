package services.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.routing.{DefaultResizer, RoundRobinPool}
import conf.connections.Configuration
import services.feeder.DatasourceService

import scala.concurrent.duration._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


object FeederActor {
  def props: Props = Props[FeederActor]
}

class FeederActor extends Actor with ActorLogging {

  val persistDataActorProps: Props = PersistDataActor.props

  val resizer: DefaultResizer =
    DefaultResizer(lowerBound = Configuration.config.getInt("custom-akka-actors.actorNumbers.lowerBound"),
      upperBound = Configuration.config.getInt("custom-akka-actors.actorNumbers.upperBound"))
  val persistDataActorActorRef: ActorRef =
    context.actorOf(RoundRobinPool(Configuration.config.getInt("custom-akka-actors.actorNumbers.start"),
      Some(resizer)).props(persistDataActorProps))


  def getData = {
    val teams = DatasourceService.apply.getTeams

    for {
      teamList <- teams
      teamsFixture <- DatasourceService.apply.getFixtureData(teamList)
      teamsRating <- DatasourceService.apply.getRatingData(teamList)
      teamsForm <- DatasourceService.apply.getFormData(teamList)
    } yield {
      println(teamList, teamsFixture)
      persistDataActorActorRef ! PersistDataActor.PersistFixtures(teamsFixture)
      persistDataActorActorRef ! PersistDataActor.PersistRatings(teamsRating)
      persistDataActorActorRef ! PersistDataActor.PersistForms(teamsForm)
      persistDataActorActorRef ! PersistDataActor.RTLearn("rt")
    }
  }

  override def receive: Receive = {
    case "START" => getData
    case _ => println(" This is Unknown Message ")
  }

}
