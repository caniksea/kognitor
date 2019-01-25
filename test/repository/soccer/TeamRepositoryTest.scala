package repository.soccer

import domain.soccer.Team
import org.scalatest.FunSuite
import repositories.soccer.TeamRepository

import scala.concurrent.Await
import scala.concurrent.duration._

class TeamRepositoryTest extends FunSuite {

  val teamId = "1"
  val teamName = "Manchester United"
  val team: Team = Team(teamId, teamName)

  val repo = TeamRepository

  val masterRepo = repo.masterImpl
  val pseudomasterRepo = repo.pseudomasterImpl

  test("createInMaster") {
    val result = Await.result(masterRepo.saveEntity(team), 2.minutes)
    println(result)
  }

  test("createInPseudomaster") {
    val result = Await.result(pseudomasterRepo.saveEntity(team), 2.minutes)
    println(result)
  }

  test("getMaster") {
    val result = Await.result(masterRepo.getEntity("1"), 2.minutes)
    println(result.get)
  }

  test("getPseudomaster") {
    val result = Await.result(pseudomasterRepo.getEntity("1"), 2.minutes)
    println(result.get)
  }

  test("getAllMaster") {
    val result = Await.result(masterRepo.getEntities, 2.minutes)
    println(result)
  }

}
