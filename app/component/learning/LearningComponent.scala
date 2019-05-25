package component.learning

//import com.cra.figaro.algorithm.learning.{EMWithBP, EMWithMH, EMWithVE}
import domain.learning.LearningResponse
import domain.soccer.Team
import services.soccer.TeamService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object LearningComponent {

  /**
    * Iterate throught the team seq and learn on each to produce a future of sequence
    *
    * @param teams
    * @param target
    * @return
    */
  def processLearnForTeams(teams: Seq[Team], target: String): Future[Seq[LearningResponse]] = {
    println("Teams: " + teams)
    val s = for {
      team <- teams
    } yield {
      println(team)
      learn(team.teamId, target)
    }

    /**
      * s is a sequence of futures, i want a future of sequence
      */
    //    val s = teams.map(team => learn(team.teamId, target)) // this produces a sequence of futures
    Future.sequence(s) // this converts the sequence of futures into future of sequence
  }

  /**
    * Learns on a team given a target (Batch or Realtime)
    *
//    * @param teamId
    * @return
    */
  def learn(teamId: String, target: String): Future[LearningResponse] = {
    val learningEngine: LearningEngine = new LearningEngine
    println(s"${teamId} has this engine: ${learningEngine}")
    for {
      team <- TeamService.masterImpl.getEntity(teamId)
      response <- learningEngine.processLearn(team, teamId, target)
    } yield {
      response
    }
  }

  /**
    * Method to learn on all teams given a target (Batch or Realtime)
    *
    * @param target
    * @return
    */
  def learnForAll(target: String): Future[Seq[LearningResponse]] = {
    println("Learning for all, target is: " + target)
    println("Getting teams...")
    for {
      teams <- TeamService.masterImpl.getEntities
      learnings <- processLearnForTeams(teams, target)
    } yield {
//      LearningEngine.stopAlgorithm()
      learnings
    }
  }

}
