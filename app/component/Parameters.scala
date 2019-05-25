package component

import com.cra.figaro.library.atomic.continuous.Beta

class PriorParameters {
  val h2hWinValue: Int = Model.numberOfH2HHomeWins + Model.numberOfH2HHomeDraws + 1
  val h2hLossValue: Int = Model.numberOfH2HHomeLosses + 1
//  val lastWinValue: Int = Model.numberOfLastWins + 1
//  val lastLoseValue: Int = (Model.numberOfLastGames - Model.numberOfLastWins) - 1

  val winProbability = Beta(4, 4)
  //  val head2headHomeWinsProbability = Beta(h2hWinValue, h2hLossValue)
  //  val formProbability = Beta(lastWinValue, lastLoseValue)
  //  val highRatingProbability = Uniform(0.6, 1)
  //  val lowRatingProbability = Uniform(0.0, 0.59)
  val goodRatingProbability = Beta(7, 4)
  val badRatingProbability = Beta(4, 7)
  val goodFormProbability = Beta(5, 3)
  val badFormProbability = Beta(3, 5)
  val goodHead2HeadProbability = Beta(5, 3)
  val badHead2HeadProbability = Beta(3, 5)
  //  val probabilities = (Seq(winProbability, head2headHomeWinsProbability, formProbability), highRatingProbability, lowRatingProbability)
  val probabilities = Seq(winProbability, goodRatingProbability, badRatingProbability, goodFormProbability, badFormProbability, goodHead2HeadProbability, badHead2HeadProbability)

}

//class PostParameters(
//                      val winProbability: Double,
//                      val head2headHomeWinsProbability: Double,
//                      val formProbability: Double,
//                      val ratingProbability: Double
//                    )

class PostParameters(
                      val winProbability: Double,
                      val goodRatingProbability: Double,
                      val badRatingProbability: Double,
                      val goodFormProbability: Double,
                      val badFormProbability: Double,
                      val goodHead2HeadProbability: Double,
                      val badHead2HeadProbability: Double
                    )
