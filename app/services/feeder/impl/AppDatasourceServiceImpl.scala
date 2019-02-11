package services.feeder.impl

import domain.feeder.AppDatasource
import repositories.feeder.AppDatasourceRepository
import services.feeder.AppDatasourceService

import scala.concurrent.Future

class AppDatasourceServiceImpl extends AppDatasourceService {
  override def getSourcesForEvent(event: String): Future[Seq[AppDatasource]] =
    AppDatasourceRepository.apply.getSourcesForEvent(event)

  override def saveEntity(entity: AppDatasource): Future[Boolean] =
    AppDatasourceRepository.apply.saveEntity(entity)

  override def getEntities: Future[Seq[AppDatasource]] =
    AppDatasourceRepository.apply.getEntities

  override def getEntity(id: String): Future[Option[AppDatasource]] =
    AppDatasourceRepository.apply.getEntity(id)

  override def deleteEntity(entity: AppDatasource): Future[Boolean] =
    AppDatasourceRepository.apply.deleteEntity(entity)

  override def createTable: Future[Boolean] =
    AppDatasourceRepository.apply.createTable
}
