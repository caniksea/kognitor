package domain.common

import java.time.{LocalDate, LocalDateTime}

trait FixtureData {
  def homeTeamGoals: Int
  def awayTeamGoals: Int
//  def dateOfCompetition: LocalDateTime
  def dateCreated: LocalDate = LocalDate.now
}
