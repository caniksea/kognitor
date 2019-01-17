package repositories.soccer

import domain.soccer.TeamProbability
import repositories.CRUDRepository
import repositories.soccer.impl.cassandra.{batchview, realtimeview}

trait TeamProbabilityRepository extends CRUDRepository[TeamProbability] {

}

object TeamProbabilityRepository {
  def batchViewImpl: TeamProbabilityRepository = new batchview.TeamProbabilityRepositoryImpl()
  def realtimeViewImpl: TeamProbabilityRepository = new realtimeview.TeamProbabilityRepositoryImpl()
}
