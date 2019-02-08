package services.setup.table

import services.setup.table.impl.CassandraSetupServiceImpl

import scala.concurrent.Future

trait CassandraSetupService {
  def createTables: Future[Boolean]
}

object CassandraSetupService {
  def apply: CassandraSetupService = new CassandraSetupServiceImpl()
}
