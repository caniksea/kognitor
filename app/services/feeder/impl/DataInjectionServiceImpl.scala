package services.feeder.impl

import component.feeder.DataInjectionComponent
import domain.feeder.{FixtureFeeder, FormFeeder, RatingFeeder}
import services.feeder.DataInjectionService

class DataInjectionServiceImpl extends DataInjectionService {
  override def saveTeamsForm(teamsForm: Seq[FormFeeder]): Unit = DataInjectionComponent.saveTeamsForm(teamsForm)

  override def saveTeamsRating(ratings: Seq[RatingFeeder]): Unit = DataInjectionComponent.saveTeamsRating(ratings)

  override def saveTeamsFixture(fixtures: Seq[FixtureFeeder]): Unit = DataInjectionComponent.saveTeamsFixture(fixtures)
}
