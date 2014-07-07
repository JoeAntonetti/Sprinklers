package model

import org.apache.http.impl.client.DefaultHttpClient
import java.util.ArrayList
import org.apache.http.NameValuePair
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.util.EntityUtils
import org.apache.http.message.BasicNameValuePair
import java.util.Arrays
import org.apache.commons.io.IOUtils

class SprinklerHTTPClient(username: String, password: String) extends DefaultHttpClient{
  
  var accountID: String = _
  var locations: List[Location] = _
  var controllers = List[Controller]()
  var currentControllerId: String = _
  var running = 0
  var notRunning = 0
  
  def login = {
     var params = new ArrayList[NameValuePair]
     params.add(new BasicNameValuePair("login", username))
     params.add(new BasicNameValuePair("password", password))
     EntityUtils.consumeQuietly(this.post(params, SprinklerHTTPClient.LOGIN).getEntity)
     accountID = getAccountId
     locations = getLocations
     locations.foreach(x => controllers = x.controllers ++ controllers)
     println(controllers)
  }
  
  def getAccountId: String = get(SprinklerHTTPClient.ACCOUNT)(0)(1).replace("\"", "")
  
  def getLocations: List[Location] = {
     var params = new ArrayList[NameValuePair]
     params.add(new BasicNameValuePair("accountId", accountID))
     params.add(new BasicNameValuePair("dataType", "json"))
     val responseData = breakResponse(post(params, SprinklerHTTPClient.LOCATIONS)).grouped(8).toList
     responseData.map(x => new Location(x, this))
  }
  
  def post(parameters: ArrayList[NameValuePair], url: String): HttpResponse = {
    var post = new HttpPost(url)
    post.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"))
    this.execute(post)
  }
  
  def get(url: String): List[List[String]] = {
    var get = new HttpGet(url)
    breakResponse(this.execute(get))
  }
  
  def breakResponse(r: HttpResponse): List[List[String]] = {
    val items = IOUtils.toString(r.getEntity().getContent()).split("\\s*,\\s*").toList
    EntityUtils.consumeQuietly(r.getEntity)
    items.map(x => x.split("\\s*:\\s*").toList)
  }
  
}

object SprinklerHTTPClient {
   def LOGIN = "https://cloud.irrigationcaddy.com/auth/login"
   def ACCOUNT = "https://cloud.irrigationcaddy.com/account/accounts"
   def LOCATIONS = "https://cloud.irrigationcaddy.com/location/locations"
   def CONTROLLERS = "https://cloud.irrigationcaddy.com/controller/controllers"
   def GET_STATUS = "https://cloud.irrigationcaddy.com/controller/getControllerStatus/" 
}
