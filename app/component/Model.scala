package component

import com.cra.figaro.language._
import com.cra.figaro.library.compound.If

abstract class Model() {
  val isWinner: Element[Boolean]
  val hasGoodRating: Element[Boolean]
  val hasGoodForm: Element[Boolean]
  val hasHomeGroundAdvantage: Element[Boolean]
//  val hasGoodStanding: Element[Boolean]

  //  val isInForm: Element[Boolean]
  //  val hasHighRating: Element[Boolean]
  //  val hasGoodStanding: Element[Boolean]
}

class LearningModel(parameters: PriorParameters) extends Model {

  val isWinner = Flip(parameters.winProbability)

//  val goodStanding = highForm && highRating
//  val badStanding = lowForm && lowRating

  val hasGoodHead2head = Flip(parameters.goodHead2HeadProbability)
  val hasBadHead2Head = Flip(parameters.badHead2HeadProbability)
  override val hasHomeGroundAdvantage = If(isWinner, hasGoodHead2head, hasBadHead2Head)

//  override val hasGoodStanding: Element[Boolean] = If(isWinner, goodStanding, badStanding)

  val highRating = Flip(parameters.goodRatingProbability)
  val lowRating = Flip(parameters.badRatingProbability)
  override val hasGoodRating = If(isWinner, highRating, lowRating)


  val highForm = Flip(parameters.goodFormProbability)
  val lowForm = Flip(parameters.badFormProbability)
  override val hasGoodForm = If(isWinner, highForm, lowForm)

}

class ReasoningModel(parameters: PostParameters) extends Model {

  val isWinner = Flip(parameters.winProbability)

  val highRating = Flip(parameters.goodRatingProbability)
  val lowRating = Flip(parameters.badRatingProbability)
  val highForm = Flip(parameters.goodFormProbability)
  val lowForm = Flip(parameters.badFormProbability)
  val hasGoodHead2head = Flip(parameters.goodHead2HeadProbability)
  val hasBadHead2Head = Flip(parameters.badHead2HeadProbability)

//  val goodStanding = highForm && highRating
//  val badStanding = lowForm && lowRating

  override val hasHomeGroundAdvantage = If(isWinner, hasGoodHead2head, hasBadHead2Head)

//  override val hasGoodStanding: Element[Boolean] = If(isWinner, goodStanding, badStanding)

  override val hasGoodRating = If(isWinner, highRating, lowRating)

  override val hasGoodForm = If(isWinner, highForm, lowForm)


//  override val hasHomeGroundAdvantage = Flip(parameters.head2headHomeWinsProbability)
//
//  override val isInForm = Flip(parameters.formProbability)
//
//  override val hasHighRating = Flip(parameters.ratingProbability)
//
//  def determinePerformance(inForm: Boolean, rating: Boolean): Element[Boolean] =
//    if (inForm && rating) Flip(0.85)
//    else if (!inForm || !rating) Flip(0.45)
//    else Constant(false)
//
//  override val hasGoodStanding = Chain(isInForm, hasHighRating, determinePerformance)
//
//  def determineWin(hga: Boolean, goodPerformance: Boolean): Element[Boolean] =
//    if(hga && goodPerformance) Flip(0.85)
//    else if (!hga || !goodPerformance) Flip(0.45)
//    else Constant(false)
//
//  val isWinner = Apply(hasHomeGroundAdvantage, hasGoodStanding, determineWin)
}

object Model {
  val numberOfLastGames = 6
  val numberOfLastWins = 3
  val numberOfH2HHome = numberOfLastGames
  val numberOfH2HHomeWins = numberOfLastWins
  val numberOfH2HHomeDraws = 0
  val numberOfH2HHomeLosses = numberOfLastWins

  val binomialGameNumber = 20
}
