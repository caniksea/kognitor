package repositories.batch

import domain.soccer.Head2Head
import repositories.CRUDRepository
import repositories.batch.impl.cassandra.master
import repositories.batch.impl.cassandra.pseudomaster

import scala.concurrent.Future

trait Head2HeadRepository extends CRUDRepository[Head2Head] {

  def getHomeTeamMatches(homeTeamId: String): Future[Seq[Head2Head]]

}

object Head2HeadRepository {
  def masterImpl: Head2HeadRepository = new master.Head2HeadRepositoryImpl()

  def pseudomasterImpl: Head2HeadRepository = new pseudomaster.Head2HeadRepositoryImpl()
}
