package services.actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.routing.DefaultResizer
import conf.connections.Configuration
import services.actors.FeederActor.CallType
import services.feeder.{CrawlerService, DatasourceService}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


object FeederActor{
  def props: Props = Props[FeederActor]

  case class CallType(form: String, fixture: String, rating: String)
}

class FeederActor  extends Actor with ActorLogging{

  val persistDataActorProps: Props = PersistDataActor.props

  val resizer: DefaultResizer =
    DefaultResizer(lowerBound = Configuration.config.getInt("custom-akka-actors.actorNumbers.lowerBound"),
      upperBound = Configuration.config.getInt("custom-akka-actors.actorNumbers.upperBound"))

  override def receive: Receive = {
    case "START" => {

      val data = DatasourceService.apply.getData()

//      for {
//        response <- crawlResponse
//      } yield {
//        persistDataActorProps ! PersistDataActor.PersistMessage(response)
//      }
      // TODO: Send Message to batch actor, and realtime actor
    }
    case _ => println(" This is Unknown Message ")
  }

}
