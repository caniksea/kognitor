package services.feeder

import domain.feeder.{FixtureFeeder, FormFeeder, RatingFeeder}
import services.feeder.impl.DataInjectionServiceImpl

import scala.concurrent.Future

trait DataInjectionService {
  def saveData(fixtures: Seq[FixtureFeeder], forms: Seq[FormFeeder], ratings: Seq[RatingFeeder]): Future[Seq[String]]
}

object DataInjectionService {
  def apply: DataInjectionService = new DataInjectionServiceImpl()
}
