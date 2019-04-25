package services.soccer

import domain.soccer.Team
import services.CRUDService
import services.soccer.impl.{master, pseudomaster}

import scala.concurrent.Future

trait TeamService extends CRUDService[Team] {
  def findByName(teamName: String): Future[Option[Team]]

}

object TeamService {
  def masterImpl: TeamService = new master.TeamServiceImpl()
  def pseudomasterImpl: TeamService = new pseudomaster.TeamServiceImpl();
}
