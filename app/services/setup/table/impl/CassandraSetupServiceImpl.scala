package services.setup.table.impl

import services.feeder.{FixtureFeederService, FormFeederService, RatingFeederService}
import services.setup.table.CassandraSetupService
import services.soccer._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class CassandraSetupServiceImpl extends CassandraSetupService {

  def createMasterTables(): Future[Boolean] = {
    FixtureService.masterImpl.createTable
    TeamService.masterImpl.createTable
    RatingService.masterImpl.createTable
    FormService.masterImpl.createTable

    FormFeederService.apply.createTable
    RatingFeederService.apply.createTable
    FixtureFeederService.apply.createTable
  }

  def createPseudomasterTables() = {
    FixtureService.pseudomasterImpl.createTable
    TeamService.pseudomasterImpl.createTable
    RatingService.pseudomasterImpl.createTable
    FormService.pseudomasterImpl.createTable
  }

  def createBatchviewTables(): Future[Boolean] = {
    TeamProbabilityService.batchViewImpl.createTable
  }

  def createRealtimeviewTables(): Future[Boolean] = {
    TeamProbabilityService.realtimeViewImpl.createTable
  }

  override def createTables: Future[Boolean] = {
    try {
      //create master tables
      createMasterTables()

      //create pseudo master tables
      createPseudomasterTables()

      // create batch view tables
      createBatchviewTables()

      // create realtime view tables
      createRealtimeviewTables()
    } catch {
      case e: Exception => {
        e.printStackTrace
        Future {false}
      }
    }
  }
}
