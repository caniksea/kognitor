package component.soccer

import component.LearningModel

class TeamHelper {

  def observeForm(model: LearningModel, form: Option[Boolean]) {
    form match {
      case Some(b) => model.isInForm.observe(b)
      case None => ()
    }
  }

  def observeHomeGroundAdv(model: LearningModel, isWin: Option[Boolean]) {
    isWin match {
      case Some(b) => model.hasHomeGroundAdvantage.observe(b)
      case None => ()
    }
  }

}
