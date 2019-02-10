package domain.datainjection

import java.time.LocalDateTime

import play.api.libs.json.Json

case class Post(zone: String,
                siteCode: String,
                year: Int,
                month: Int,
                date: Int,
                linkhash: String,
                datePublished: LocalDateTime,
                title: String,
                article: String,
                metakeywords: String,
                metaDescription: String,
                articleLink: String,
                imageUrl: String,
                seo: String,
                imagePath: String,
                caption: String)

object Post {

  implicit val postbjectFmt = Json.format[Post]

  def identity: Post = Post("", "", 0, 0, 0, "", LocalDateTime.now(), "", "", "", "", "", "", "", "", "")
}

