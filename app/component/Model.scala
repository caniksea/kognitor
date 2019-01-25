package component

import com.cra.figaro.language._

abstract class Model() {
  val hasHomeGroundAdvantage: Element[Boolean]
  val isInForm: Element[Boolean]
  val hasHighRating: Element[Boolean]
  val hasGoodStanding: Element[Boolean]
//  val isWinner
}

class LearningModel(parameters: PriorParameters) extends Model {
  override val hasHomeGroundAdvantage = Flip(parameters.head2headHomeWinsProbability)

  override val isInForm = Flip(parameters.formProbability)

  override val hasHighRating = Flip(parameters.ratingProbability)

  def determinePerformance(inForm: Boolean, rating: Boolean): Element[Boolean] =
    if (inForm && rating) Flip(0.85)
    else if (inForm && !rating) Flip(0.55)
    else if (!inForm && rating) Flip(0.4)
    else Constant(false)

  override val hasGoodStanding = Chain(isInForm, hasHighRating, determinePerformance)

  def determineWin(hga: Boolean, goodPerformance: Boolean): Element[Boolean] =
    if(hga && goodPerformance) Flip(0.85)
    else if (!hga && goodPerformance) Flip(0.65)
    else if (hga && !goodPerformance) Flip(0.45)
    else Constant(false)

  val isWinner = Apply(hasHomeGroundAdvantage, hasGoodStanding, determineWin)
}

class ReasoningModel(parameters: PostParameters) extends Model {
  override val hasHomeGroundAdvantage = Flip(parameters.head2headHomeWinsProbability)

  override val isInForm = Flip(parameters.formProbability)

  override val hasHighRating = Flip(parameters.ratingProbability)

  def determinePerformance(inForm: Boolean, rating: Boolean): Element[Boolean] =
    if (inForm && rating) Flip(0.85)
    else if (inForm && !rating) Flip(0.55)
    else if (!inForm && rating) Flip(0.4)
    else Constant(false)

  override val hasGoodStanding = Chain(isInForm, hasHighRating, determinePerformance)

  def determineWin(hga: Boolean, goodPerformance: Boolean): Element[Boolean] =
    if(hga && goodPerformance) Flip(0.85)
    else if (!hga && goodPerformance) Flip(0.65)
    else if (hga && !goodPerformance) Flip(0.45)
    else Constant(false)

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
