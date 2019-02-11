package conf.connections

import com.typesafe.config.{Config, ConfigFactory}
import okhttp3.{MediaType, OkHttpClient, Request, RequestBody}

object NetConnection {

  lazy val apiUrl : String = Configuration.config.getString("api.baseUrl")
  lazy val crawler: String = Configuration.config.getString("crawler.URI")
  lazy val schema  = Configuration.config.getBoolean("schema.status")
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
