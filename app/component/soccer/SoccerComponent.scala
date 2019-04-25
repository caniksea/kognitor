package component.soccer

import domain.soccer.Team
import services.soccer.TeamService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object SoccerComponent {

  def findByName(teamName: String, teamService: TeamService): Future[Option[Team]] = {
    for {
      teams <- teamService.getEntities
    } yield {
      filterByName(teams, teamName.trim.toLowerCase)
    }
  }


  def filterByName(teams: Seq[Team], teamName: String) = {
    teams.find(team => {
      val storedTeamName = team.teamName.toLowerCase
      storedTeamName.equals(teamName) || storedTeamName.contains(teamName) || teamName.contains(storedTeamName)
    })
  }


}
