package component.soccer

import component.Model

class TeamHelper {

  def observeForm(model: Model, form: Option[Boolean]) {
    form match {
      case Some(b) => model.hasGoodForm.observe(b)
      case None => ()
    }
  }

  def observeHomeGroundAdv(model: Model, isWin: Option[Boolean]) {
    isWin match {
      case Some(b) => model.hasHomeGroundAdvantage.observe(b)
      case None => ()
    }
  }

}
