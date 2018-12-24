package component

import com.cra.figaro.language.Element
import com.cra.figaro.library.atomic.continuous.{Beta, Uniform}


abstract class Parameters {
  val head2headHomeWinsProbability: Element[Double]
  val formProbability: Element[Double]
  val ratingProbability: Element[Double]
}

class PriorParameters extends Parameters {
  val h2hWinValue: Int = Model.numberOfH2HHomeWins + Model.numberOfH2HHomeDraws + 1
  val h2hLossValue: Int = Model.numberOfH2HHomeLosses + 1
  val lastWinValue: Int = Model.numberOfLastWins + 1;
  val lastLoseValue: Int = (Model.numberOfLastGames - Model.numberOfLastWins) - 1

  override val head2headHomeWinsProbability: Element[Double] = Beta(h2hWinValue, h2hLossValue)
  override val formProbability = Beta(lastWinValue, lastLoseValue)
  override val ratingProbability = Uniform(6.00, 10)
}

class PostParameters(
                         override val head2headHomeWinsProbability: Element[Double],
                         override val formProbability: Element[Double],
                         override val ratingProbability: Element[Double]
                       ) extends Parameters {}
