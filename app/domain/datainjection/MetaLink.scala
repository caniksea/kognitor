package domain.datainjection

import play.api.libs.json.Json

case class MetaLink(zone: String,
                    datePublished: String,
                    linkHash: String,
                    linkUrl: String,
                    linkSite: String,
                    linkTitle: String,
                    linkType: String,
                    linkSiteCode: String)

object MetaLink{
  implicit val metaLinkFormat = Json.format[MetaLink]
}
