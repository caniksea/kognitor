package repositories.batch.master

import domain.soccer.Head2Head
import repositories.Repository
import repositories.batch.master.impl.Head2HeadRepositoryImpl

import scala.concurrent.Future

trait Head2HeadRepository extends Repository[Head2Head] {

  def getHomeTeamMatches(homeTeamId: String): Future[Seq[Head2Head]]

}

object Head2HeadRepository {
  def apply: Head2HeadRepository = new Head2HeadRepositoryImpl()
}
