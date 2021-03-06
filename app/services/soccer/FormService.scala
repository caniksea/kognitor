package services.soccer

import java.time.LocalDate

import domain.soccer.Form
import services.CRUDService
import services.soccer.impl.{master, pseudomaster}

import scala.concurrent.Future

trait FormService extends CRUDService[Form] {
  def getTeamFormForDate(teamId: String, date: LocalDate = LocalDate.now()): Future[Seq[Form]]
  def getTeamForms(teamId: String): Future[Seq[Form]]
  def clear: Future[Boolean]
  def deleteByTeamId(teamId: String): Future[Boolean]
}

object FormService {
  def masterImpl: FormService = new master.FormServiceImpl()
  def pseudomasterImpl: FormService = new pseudomaster.FormServiceImpl()
}
