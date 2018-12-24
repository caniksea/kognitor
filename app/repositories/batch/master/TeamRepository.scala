package repositories.batch.master

import repositories.Repository
import repositories.batch.master.impl.TeamRepositoryImpl

trait TeamRepository extends Repository[TeamRepository]{

}

object TeamRepository {
  def apply: TeamRepository = new TeamRepositoryImpl()
}
