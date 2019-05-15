package services.feeder

import domain.feeder.{FeederData, FixtureFeeder, FormFeeder, RatingFeeder}
import domain.soccer.Team
import services.feeder.impl.DatasourceServiceImpl

import scala.concurrent.Future

trait DatasourceService {

//  def getFixtureData(teamList: Seq[Team]): Future[Seq[FixtureFeeder]]

//  def getTeams: Future[Seq[Team]]

//  def getRatingData(teamList: Seq[Team]): Future[Seq[RatingFeeder]]

//  def getFormData(teamList: Seq[Team]): Future[Seq[FormFeeder]]

  def getData: Future[(Seq[FixtureFeeder], Seq[FormFeeder], Seq[RatingFeeder])]
}

object DatasourceService {
  def apply: DatasourceService = new DatasourceServiceImpl()
}
