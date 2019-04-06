package services.feeder.impl

import java.time.LocalDate

import domain.feeder.FormFeeder
import repositories.feeder.FormFeederRepository
import services.feeder.FormFeederService

import scala.concurrent.Future

class FormFeederServiceImpl extends FormFeederService {
  override def getTeamForm(teamName: String, date: LocalDate): Future[Option[FormFeeder]] =
    FormFeederRepository.apply.getTeamForm(teamName, date)

  override def saveEntity(entity: FormFeeder): Future[Boolean] =
    FormFeederRepository.apply.saveEntity(entity)

  override def getEntities: Future[Seq[FormFeeder]] = ???

  override def getEntity(id: String): Future[Option[FormFeeder]] = ???

  override def deleteEntity(entity: FormFeeder): Future[Boolean] = ???

  override def createTable: Future[Boolean] = FormFeederRepository.apply.createTable
}
