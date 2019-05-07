package domain.common

import java.time.LocalDate

trait FormData {
  def numberOfWins: Int
  def numberOfLoses: Int
  def numberOfDraws: Int
  def sourceDate: LocalDate = LocalDate.now
}
