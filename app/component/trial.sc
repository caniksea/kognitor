import cats.Monoid
import cats.implicits._
import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.language.Constant
import com.cra.figaro.library.atomic.continuous.{Beta, Uniform}
import component.soccer.TeamHelper
import component._



implicit val intAdditionMonoid: Monoid[Int] = new Monoid[Int] {
  def empty: Int = 0
  def combine(x: Int, y: Int): Int = x + y
}

case class Result(name:String, score:Int)

object Result{
  implicit val resMonoid :Monoid[Result] = new Monoid[Result]{
    override def empty: Result = Result("Sum of: ", 0)

    override def combine(x: Result, y: Result): Result = {
      val res = x.score+y.score
      val name = x.name+" " +y.name
      Result(name,res)
    }
  }
}

val scores = List( Result("res1",2), Result("res2", 5),Result("Res3",10))

Monoid.combineAll(scores)


val re = Seq(3, 2, 4, 2, 5)
val er = Seq(2, 5)
val ee = re.take(4)

val help = Beta(2.5, 3.5)

val you = Uniform(0.6, 1)
val youd = you.generateRandomness()

val params = new PriorParameters
val formP = params.formProbability.MAPValue
val h2hP = params.head2headHomeWinsProbability.MAPValue
val ratingP = params.probabilities._2.generateRandomness()
val post = new PostParameters(h2hP, formP, youd)
val model: ReasoningModel = new ReasoningModel(post)
new TeamHelper().observeForm(model, Some(true))
val algorithm = VariableElimination()
algorithm.start()
val isWinProbability = VariableElimination.probability(model.isWinner.value, true)
algorithm.kill()