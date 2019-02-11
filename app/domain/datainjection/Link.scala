package domain.datainjection

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import play.api.libs.json.{Json, Reads, Writes}
import cats.syntax.either._

case class Link(zone: String,
                datePublished: LocalDateTime,
                linkHash: String,
                linkUrl: String,
                linkSite: String,
                linkTitle: String,
                linkType: String,
                linkSiteCode: String)

object Link {



  val dateFormat = "yyMMddHHmmssZ" //
  implicit val dateTimeReads = Reads.dateReads(dateFormat)
  implicit val dateTimeWrites = Writes.dateWrites(dateFormat)
  implicit val linkFmt = Json.format[Link]

  val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
  implicit val dateEncoder = Encoder.encodeString.contramap[LocalDateTime](_.format(formatter))
  implicit val dateDecoder = Decoder.decodeString.emap[LocalDateTime](str => {
    Either.catchNonFatal(LocalDateTime.parse(str, formatter)).leftMap(_.getMessage)
  })
  implicit val AEncoder = deriveEncoder[Link]
  implicit val ADecoder = deriveDecoder[Link]

  def identity: Link = Link("", LocalDateTime.now(), "", "", "", "", "", "")

}

