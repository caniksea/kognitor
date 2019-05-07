package services.actors

import akka.actor.{Actor, ActorLogging, Props}
import domain.datainjection.Post
import domain.feeder.{FixtureFeeder, FormFeeder, RatingFeeder}
import services.actors.PersistDataActor.{PersistFixtures, PersistForms, PersistMessage, PersistRatings, RTLearn}
import services.feeder.DataInjectionService
import services.learning.LearningService

object PersistDataActor {
  val props: Props = Props[PersistDataActor]

  case class PersistMessage(message: Post)

  case class PersistForms(forms: Seq[FormFeeder])

  case class PersistRatings(ratings: Seq[RatingFeeder])

  case class PersistFixtures(fixtures: Seq[FixtureFeeder])

  case class RTLearn(target: String)

}

class PersistDataActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case post: PersistMessage => {
      if (post.message.title.length != 0) {
        // Save Service Here
      }

    }
    case formCase: PersistForms =>
      val forms = formCase.forms
      if (!forms.isEmpty) DataInjectionService.apply.saveTeamsForm(forms)
    case fixtureCase: PersistFixtures =>
      val fixtures = fixtureCase.fixtures
      if (!fixtures.isEmpty) DataInjectionService.apply.saveTeamsFixture(fixtures)
    case ratingCase: PersistRatings =>
      val ratings = ratingCase.ratings
      if (!ratings.isEmpty) DataInjectionService.apply.saveTeamsRating(ratings)
    case learn: RTLearn =>
      LearningService.apply.learnForAll(learn.target)
    case _ => println("Unknown message type!")

  }
}

