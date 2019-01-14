package domain.soccer

import play.api.libs.json.Json

@Deprecated
class H2HHomeSummary(
                homeTeamId: String,
                awayTeamId: String,
                numberOfH2HHomeWins: Int,
                numberOfH2HHomeDraws: Int,
                numberOfH2HHomeLoses: Int,
                ) {}

object H2HHomeSummary {
  implicit val h2HHomeSummaryFormat = Json.format[H2HHomeSummary]
}
