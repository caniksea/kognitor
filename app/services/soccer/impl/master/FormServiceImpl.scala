package services.soccer.impl.master

import java.time.LocalDate

import domain.soccer.Form
import repositories.soccer.FormRepository
import services.soccer.FormService

import scala.concurrent.Future

class FormServiceImpl extends FormService {
  override def getTeamForms(teamId: String): Future[Seq[Form]] =
    FormRepository.masterImpl.getTeamForms(teamId)

  override def saveEntity(entity: Form): Future[Boolean] =
    FormRepository.masterImpl.saveEntity(entity)

  override def getEntities: Future[Seq[Form]] =
    FormRepository.masterImpl.getEntities

  override def getEntity(teamId: String): Future[Option[Form]] =
    FormRepository.masterImpl.getEntity(teamId)

  override def createTable: Future[Boolean] =
    FormRepository.masterImpl.createTable

  override def deleteEntity(entity: Form): Future[Boolean] =
    FormRepository.masterImpl.deleteEntity(entity)

  override def clear: Future[Boolean] = ???

  override def deleteByTeamId(teamId: String): Future[Boolean] = ???

  override def getTeamFormForDate(teamId: String, date: LocalDate): Future[Seq[Form]] = ???
}
