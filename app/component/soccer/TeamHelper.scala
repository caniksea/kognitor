package component.soccer

import component.Model

class TeamHelper {

  def observeEvidence(model: Model, hasHGA: Option[Boolean], hasForm: Option[Boolean], hasGoodRating: Option[Boolean]) {
    hasForm match {
      case Some(value) => model.hasGoodForm.observe(value)
      case None => ()
    }

    hasHGA match {
      case Some(value) => model.hasHomeGroundAdvantage.observe(value)
      case None => ()
    }

    hasGoodRating match {
      case Some(value) => model.hasGoodRating.observe(value)
      case None => ()
    }
  }

}
