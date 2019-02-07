package component

import com.cra.figaro.language.Element
import com.cra.figaro.library.atomic.continuous.{Beta, Uniform}


//abstract class Parameters {
//  val head2headHomeWinsProbability: Double
//  val formProbability: Double
//  val ratingProbability: Double
//}

class PriorParameters {
  val h2hWinValue: Int = Model.numberOfH2HHomeWins + Model.numberOfH2HHomeDraws + 1
  val h2hLossValue: Int = Model.numberOfH2HHomeLosses + 1
  val lastWinValue: Int = Model.numberOfLastWins + 1;
  val lastLoseValue: Int = (Model.numberOfLastGames - Model.numberOfLastWins) - 1

  val head2headHomeWinsProbability = Beta(h2hWinValue, h2hLossValue)
  val formProbability = Beta(lastWinValue, lastLoseValue)
  val ratingProbability = Uniform(0.6, 1)
  val probabilities = (Seq(head2headHomeWinsProbability, formProbability), ratingProbability)

}

class PostParameters(
                      val head2headHomeWinsProbability: Double,
                      val formProbability: Double,
                      val ratingProbability: Double
                    )
