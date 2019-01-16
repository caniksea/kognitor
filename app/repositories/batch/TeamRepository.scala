package repositories.batch

import domain.soccer.{Team}
import repositories.CRUDRepository
import repositories.batch.impl.cassandra.{master, pseudomaster}

trait TeamRepository extends CRUDRepository[Team] {
}

object TeamRepository {
  def masterImpl: TeamRepository = new master.TeamRepositoryImpl()

  def pseudomasterImpl: TeamRepository = new pseudomaster.TeamRepositoryImpl()
}
