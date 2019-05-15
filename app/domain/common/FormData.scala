package domain.common

import java.time.LocalDate

trait FormData {
  def numberOfWins: Int
  def numberOfLoses: Int
  def numberOfDraws: Int
  def dateCreated: LocalDate = LocalDate.now
}
