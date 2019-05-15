package component.feeder

import domain.feeder.{FixtureFeeder, FormFeeder, RatingFeeder}
import domain.soccer.{Fixture, Form, Rating, Team}
import services.soccer.{FixtureService, FormService, RatingService}

import scala.concurrent.ExecutionContext.Implicits.global

object DataInjectionComponent {

  def saveData(fixtures: Seq[FixtureFeeder], forms: Seq[FormFeeder], ratings: Seq[RatingFeeder]) = {
    for {
      teams <- DatasourceComponent.getTeams
    } yield {
      saveTeamsRating(teams, ratings)
      saveTeamsFixture(teams, fixtures)
      saveTeamsForm(teams, forms)
      teams.map(team => team.teamId)
    }
  }


  def saveTeamsFixture(teams: Seq[Team], fixtures: Seq[FixtureFeeder]) = {
    fixtures.foreach(fixture => {
      val team = teams.filter(team => team.teamName.trim.toLowerCase.equals(fixture.teamName.trim.toLowerCase)).head
      if (team != null){
        val awayTeam = teams.filter(team => team.teamName.trim.toLowerCase.equals(fixture.awayTeamName.trim.toLowerCase)).head
        if (awayTeam != null) {
          val f: Fixture = Fixture(team.teamId, awayTeam.teamId, fixture.homeTeamGoals, fixture.awayTeamGoals, fixture.dateCreated)
          FixtureService.masterImpl.saveEntity(f)
          FixtureService.pseudomasterImpl.saveEntity(f)
        }
      }
    })
  }

//  def saveTeamsFixture(fixtures: Seq[FixtureFeeder]): Unit = {
//    for {
//      teams <- DatasourceComponent.getTeams
//    } yield {
//      processFixtureSave(teams, fixtures)
//    }
//  }

  def saveTeamsRating(teams: Seq[Team], ratings: Seq[RatingFeeder]) = {
    ratings.foreach(rating => {
      val team = teams.filter(team => team.teamName.trim.toLowerCase.equals(rating.teamName.trim.toLowerCase)).head
      if (team != null) {
        val r: Rating = Rating(team.teamId, rating.rating, rating.dateCreated)
        RatingService.masterImpl.saveEntity(r)
        RatingService.pseudomasterImpl.saveEntity(r)
      }
    })
  }

//  def saveTeamsRating(ratings: Seq[RatingFeeder]): Unit = {
//    for {
//      teams <- DatasourceComponent.getTeams
//    } yield {
//      processRatingSave(teams, ratings)
//    }
//  }


  def saveTeamsForm(teams: Seq[Team], teamsForm: Seq[FormFeeder]) = {
    teamsForm.foreach(teamForm => {
      val team: Team = teams.find(team => team.teamName.trim.equalsIgnoreCase(teamForm.teamName.trim)).getOrElse(null)
//      val team: Team = teams.filter(team => team.teamName.trim.toLowerCase == teamForm.teamName.trim.toLowerCase).head
      if (team != null) {
        val form: Form = Form(team.teamId, teamForm.numberOfWins, teamForm.numberOfLoses, teamForm.numberOfDraws, teamForm.dateCreated)
        FormService.masterImpl.saveEntity(form)
        FormService.pseudomasterImpl.saveEntity(form)
      }
    })
  }

//  def saveTeamsForm(teamsForm: Seq[FormFeeder]): Unit = {
//    for {
//      teams <- DatasourceComponent.getTeams
//    } yield {
//      processFormSave(teams, teamsForm)
//    }
//  }

}
