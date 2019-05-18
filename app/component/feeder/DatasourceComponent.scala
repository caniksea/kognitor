package component.feeder

import java.time.LocalDate

import domain.feeder.{FixtureFeeder, FormFeeder, RatingFeeder}
import domain.soccer.Team
import services.feeder.{FixtureFeederService, FormFeederService, RatingFeederService}
import services.soccer.TeamService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object DatasourceComponent {

  val today = LocalDate.parse("2018-10-20")

  def getTeams: Future[Seq[Team]] = TeamService.masterImpl.getEntities

  def getTeamNames(teamList: Seq[Team]): List[String] = teamList.map(team => team.teamName).toList

  def getFormData(teamList: Seq[Team]): Future[Seq[FormFeeder]] = {
    val teamNames = getTeamNames(teamList)
    FormFeederService.apply.getTeamsForm(teamNames, today)
  }

  def getFixtureData(teamList: Seq[Team]): Future[Seq[FixtureFeeder]] = {
    val teamNames: List[String] = getTeamNames(teamList)
    FixtureFeederService.apply.getTeamsFixture(teamNames, today)
  }

  def getRatingData(teamList: Seq[Team]): Future[Seq[RatingFeeder]] = {
    val teamNames = getTeamNames(teamList)
    RatingFeederService.apply.getTeamsRating(teamNames, today)
  }

  def getData: Future[(Seq[FixtureFeeder], Seq[FormFeeder], Seq[RatingFeeder])] = {
    for {
      teams <- TeamService.masterImpl.getEntities
      fixtureData <- getFixtureData(teams)
      ratingData <- getRatingData(teams)
      formData <- getFormData(teams)
    } yield {
      (fixtureData, formData, ratingData)
    }
  }

}
