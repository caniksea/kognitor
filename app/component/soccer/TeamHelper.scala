package component.soccer

import component.Model

class TeamHelper {

  def observeEvidence(model: Model, hasHGA: Option[Boolean], hasForm: Option[Boolean]) {
    hasForm match {
      case Some(value) => model.hasGoodForm.observe(value)
      case None => ()
    }

    hasHGA match {
      case Some(value) => model.hasHomeGroundAdvantage.observe(value)
      case None => ()
    }
  }

}
