package services.soccer

import domain.soccer.Team
import services.CRUDService
import services.soccer.impl.{master, pseudomaster}

trait TeamService extends CRUDService[Team] {

}

object TeamService {
  def masterImpl: TeamService = new master.TeamServiceImpl()
  def pseudomasterImpl: TeamService = new pseudomaster.TeamServiceImpl();
}
