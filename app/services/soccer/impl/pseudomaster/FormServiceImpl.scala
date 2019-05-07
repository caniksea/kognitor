package services.soccer.impl.pseudomaster

import domain.soccer.Form
import repositories.soccer.FormRepository
import services.soccer.FormService

import scala.concurrent.Future

class FormServiceImpl extends FormService {
  override def getTeamForms(teamId: String): Future[Seq[Form]] =
    FormRepository.pseudomasterImpl.getTeamForms(teamId)

  override def saveEntity(entity: Form): Future[Boolean] =
    FormRepository.pseudomasterImpl.saveEntity(entity)

  override def getEntities: Future[Seq[Form]] =
    FormRepository.pseudomasterImpl.getEntities

  override def getEntity(teamId: String): Future[Option[Form]] =
    FormRepository.pseudomasterImpl.getEntity(teamId)

  override def createTable: Future[Boolean] =
    FormRepository.pseudomasterImpl.createTable

  override def deleteEntity(entity: Form): Future[Boolean] =
    FormRepository.pseudomasterImpl.deleteEntity(entity)

  override def clear: Future[Boolean] = FormRepository.pseudomasterImpl.clear
}
