package services.feeder

import domain.feeder.{FixtureFeeder, FormFeeder, RatingFeeder}
import services.feeder.impl.DataInjectionServiceImpl

trait DataInjectionService {
  def saveTeamsRating(ratings: Seq[RatingFeeder])

  def saveTeamsFixture(fixtures: Seq[FixtureFeeder])

  def saveTeamsForm(teamsForm: Seq[FormFeeder])
}

object DataInjectionService {
  def apply: DataInjectionService = new DataInjectionServiceImpl()
}
