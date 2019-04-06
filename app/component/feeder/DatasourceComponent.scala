package component.feeder

import java.time.LocalDate

import domain.feeder.{FeederData}
import domain.soccer.Team
import services.feeder.{FixtureFeederService, FormFeederService, RatingFeederService}
import services.soccer.TeamService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object DatasourceComponent {

  val today = LocalDate.now

  def getTeamRating(teamName: String) = {
    RatingFeederService.apply.getTeamRating(teamName, today)
  }

  def getTeamsRating(teamList: Seq[Team]) = {
    for {
      team <- teamList
    } yield {
      getTeamRating(team.teamName)
    }
  }

  def getTeamsForm(teamList: Seq[Team]) = {
    for {
      team <- teamList
    } yield {
      FormFeederService.apply.getTeamForm(team.teamName, today)
    }
  }

  def getTeamsFixture(teamList: Seq[Team]) = {
    for {
      team <- teamList
    } yield {
      FixtureFeederService.apply.getTeamFixture(team.teamName, today)
    }
  }

  def getData(): Future[Seq[FeederData]] = {
    val teams: Future[Seq[Team]] = TeamService.masterImpl.getEntities
    for {
      teamList <- teams
    } yield {
      val teamsRating = getTeamsRating(teamList)
      val teamsForm = getTeamsForm(teamList)
      val teamsFixture = getTeamsFixture(teamList)
    }

    null
  }

}
