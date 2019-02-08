package services.soccer

import domain.soccer.TeamProbability
import services.CRUDService
import services.soccer.impl.{batchview, realtimeview}

trait TeamProbabilityService extends CRUDService[TeamProbability] {

}


object TeamProbabilityService {
  def batchViewImpl: TeamProbabilityService = new batchview.TeamProbabilityServiceImpl()
  def realtimeViewImpl: TeamProbabilityService = new realtimeview.TeamProbabilityServiceImpl()
}
