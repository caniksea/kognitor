import cats.Monoid
import cats.implicits._
import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.library.atomic.continuous.Uniform
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

val you = Uniform(6.00, 10)
val youd = you.generateRandomness()

//val params = new PriorParameters
//val formP = params.formProbability.MAPValue
//val post =
//  new PostParameters(params.head2headHomeWinsProbability.MAPValue,
//    formP,
//    params.ratingProbability.generateRandomness())
//val model: Model = new ReasoningModel(post)
//new TeamHelper().observeForm(model, Some(false))
//val algorithm = VariableElimination(model.isInForm)
//algorithm.start()
//val isWinProbability = algorithm.probability(model.isInForm, false)
//println("Win probability: " + isWinProbability)