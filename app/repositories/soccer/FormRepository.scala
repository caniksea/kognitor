package repositories.soccer

import domain.soccer.Form
import repositories.CRUDRepository
import repositories.soccer.impl.cassandra.{master, pseudomaster}

import scala.concurrent.Future

trait FormRepository extends CRUDRepository[Form] {
  def getTeamForms(teamId: String): Future[Seq[Form]]
  def clear: Future[Boolean]
}

object FormRepository {
  def masterImpl: FormRepository = new master.FormRepositoryImpl()
  def pseudomasterImpl: FormRepository = new pseudomaster.FormRepositoryImpl()
}
