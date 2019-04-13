package services.feeder.impl

import component.feeder.DatasourceComponent
import domain.feeder.{FeederData, FixtureFeeder, FormFeeder, RatingFeeder}
import domain.soccer.Team
import services.feeder.DatasourceService

import scala.concurrent.Future

class DatasourceServiceImpl extends DatasourceService {
  override def getRatingData(teamList: Seq[Team]): Future[Seq[RatingFeeder]] = DatasourceComponent.getRatingData(teamList);

  override def getFormData(teamList: Seq[Team]): Future[Seq[FormFeeder]] =
    DatasourceComponent.getFormData(teamList)

  override def getTeams: Future[Seq[Team]] = DatasourceComponent.getTeams;

  override def getFixtureData(teamList: Seq[Team]): Future[Seq[FixtureFeeder]] = DatasourceComponent.getFixtureData(teamList)
}
