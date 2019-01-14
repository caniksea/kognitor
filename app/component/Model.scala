package component

import com.cra.figaro.language._

class Model(parameters: Parameters){

  val hasHomeGroundAdvantage = Flip(parameters.head2headHomeWinsProbability)
  val form: Element[Boolean] = Flip(parameters.formProbability)
  val rating = Apply(parameters.ratingProbability,
    (i: Double) => if(i >= 7) "good"; else if (i >= 6 && i < 7) "average"; else "poor")

  def determinePerformance(inForm: Boolean, rating: String): Element[Boolean] =
    if(inForm){
      if(rating.equalsIgnoreCase("good"))
        Flip(0.8)
      else if (rating.equalsIgnoreCase("average"))
        Flip(0.5)
      else Flip(0.3)
    } else Constant(false)

  val hasGoodStanding = Chain(form, rating, determinePerformance)

  def determineWin(hga: Boolean, goodPerformance: Boolean): Element[Boolean] =
    if(hga && goodPerformance)
      Flip(0.85)
    else if(!hga && goodPerformance)
      Flip(0.5)
    else Flip(0.15)

  val isWinner = Apply(hasHomeGroundAdvantage, hasGoodStanding, determineWin)
}

object Model {
  val numberOfLastGames = 6
  val numberOfLastWins = 3
  val numberOfH2HHome = numberOfLastGames
  val numberOfH2HHomeWins = numberOfLastWins
  val numberOfH2HHomeDraws = 0
  val numberOfH2HHomeLosses = numberOfLastWins
}
