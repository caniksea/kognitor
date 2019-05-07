package component.feeder

import java.time.LocalDate

import domain.feeder.{FixtureFeeder, FormFeeder, RatingFeeder}
import domain.soccer.Team
import services.feeder.{FixtureFeederService, FormFeederService, RatingFeederService}
import services.soccer.TeamService

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object DatasourceComponent {

  def getFormData(teamList: Seq[Team]): Future[Seq[FormFeeder]] = {
    val teamNames = getTeamNames(teamList)
    FormFeederService.apply.getTeamsForm(teamNames, today)
  }

  def getTeamNames(teamList: Seq[Team]): List[String] = teamList.map(team => team.teamName).toList

  def getFixtureData(teamList: Seq[Team]): Future[Seq[FixtureFeeder]] = {
    val teamNames: List[String] = getTeamNames(teamList)
    FixtureFeederService.apply.getTeamsFixture(teamNames, today);
  }

  def getTeams: Future[Seq[Team]] = TeamService.masterImpl.getEntities

  def getRatingData(teamList: Seq[Team]): Future[Seq[RatingFeeder]] = {
    val teamNames = getTeamNames(teamList)
    RatingFeederService.apply.getTeamsRating(teamNames, today)
  }

  val today = LocalDate.parse("2019-04-24")

}
