package conf.connections

import com.typesafe.config.{Config, ConfigFactory}
import okhttp3.{MediaType, OkHttpClient, Request, RequestBody}

object NetConnection {

  lazy val config: Config = ConfigFactory.load()
  lazy val apiUrl : String = config.getString("api.baseUrl")
  lazy val crawler: String = config.getString("crawler.URI")
  lazy val schema  = config.getBoolean("schema.status")
  implicit val mediaType: MediaType = MediaType.parse("application/json")


  def getClient: OkHttpClient ={
    new OkHttpClient()
  }

  def buildRequest(url:String, jsondata:String) (implicit mediaType: MediaType): Request ={
   new Request.Builder()
      .url(url)
      .post(RequestBody.create(mediaType, jsondata))
      .build()
  }


}
