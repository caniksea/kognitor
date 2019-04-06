package domain.common

import java.time.{LocalDate}

trait RatingData {
  def rating: Double
  def dateCreated: LocalDate = LocalDate.now
}
