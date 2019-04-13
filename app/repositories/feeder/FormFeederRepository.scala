package repositories.feeder

import java.time.LocalDate

import domain.feeder.FormFeeder
import repositories.CRUDRepository
import repositories.feeder.impl.cassandra.FormFeederRepositoryImpl

import scala.concurrent.Future

trait FormFeederRepository extends CRUDRepository[FormFeeder] {
  def getTeamsForm(teamNames: List[String], date: LocalDate): Future[Seq[FormFeeder]]

  def getTeamForm(teamName: String, date: LocalDate): Future[Option[FormFeeder]]
}

object FormFeederRepository {
  def apply: FormFeederRepository = new FormFeederRepositoryImpl()
}