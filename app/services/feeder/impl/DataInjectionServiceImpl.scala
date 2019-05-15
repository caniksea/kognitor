package services.feeder.impl

import component.feeder.DataInjectionComponent
import domain.feeder.{FixtureFeeder, FormFeeder, RatingFeeder}
import services.feeder.DataInjectionService

import scala.concurrent.Future

class DataInjectionServiceImpl extends DataInjectionService {
  override def saveData(fixtures: Seq[FixtureFeeder], forms: Seq[FormFeeder], ratings: Seq[RatingFeeder]): Future[Seq[String]] =
    DataInjectionComponent.saveData(fixtures, forms, ratings)
}
