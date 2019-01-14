package domain.soccer

import play.api.libs.json.Json

case class TeamProbability(
                          teamId: String,
                          head2headHomeWinsProbability: Double,
                          ratingProbability: Double,
                          formProbability: Double
                          ){}

object TeamProbability{
  implicit val teamProbabilityFormat = Json.format[TeamProbability]
}
