package services.feeder

import java.time.LocalDate

import domain.feeder.FormFeeder
import services.CRUDService
import services.feeder.impl.FormFeederServiceImpl

import scala.concurrent.Future

trait FormFeederService extends CRUDService[FormFeeder] {
  def getTeamsForm(teamNames: List[String], date: LocalDate): Future[Seq[FormFeeder]]

  def getTeamForm(teamName: String, date: LocalDate): Future[Option[FormFeeder]]
}

object FormFeederService {
  def apply: FormFeederService = new FormFeederServiceImpl()
}
