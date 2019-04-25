package domain.soccer

import play.api.libs.json.Json

case class TeamProbability(
                          teamId: String,
                          winProbability: Double,
                          goodRatingProbability: Double,
                          badRatingProbability: Double,
                          goodFormProbability: Double,
                          badFormProbability: Double,
                          goodHead2HeadProbability: Double,
                          badHead2HeadProbability: Double
                          ){}

object TeamProbability{
  implicit val teamProbabilityFormat = Json.format[TeamProbability]
  def identity = TeamProbability("", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
}
