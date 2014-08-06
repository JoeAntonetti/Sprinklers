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
import com.eclipsesource.json.JsonArray
import com.eclipsesource.json.JsonObject

class SprinklerHTTPClient(username: String, password: String, setFlow: String) extends DefaultHttpClient{
  
  var accountID: String = _
  var locations: List[Location] = _
  var controllers = List[Controller]()
  var currentControllerId: String = _
  var running = 0
  var notRunning = 0
  var runningControllers = List[Controller]()
  var notRunningControllers = List[Controller]()
  val flow = setFlow.toInt
  
  def login = {
     var params = new ArrayList[NameValuePair]
     params.add(new BasicNameValuePair("login", username))
     params.add(new BasicNameValuePair("password", password))
     this.post(params, SprinklerHTTPClient.LOGIN)
     accountID = getAccountId
     locations = getLocations
     locations.foreach(x => controllers = x.controllers ++ controllers)
  }
  
  def getAccountId: String = JsonArray.readFrom(get(SprinklerHTTPClient.ACCOUNT)).get(0).asObject.get("accountId").asString()
  
  def getLocations: List[Location] = {
     var params = new ArrayList[NameValuePair]
     params.add(new BasicNameValuePair("accountId", accountID))
     params.add(new BasicNameValuePair("dataType", "json"))
     val responseData = post(params, SprinklerHTTPClient.LOCATIONS)
     val jsonArr = JsonArray.readFrom(responseData)
     var locations = List[Location]()
     for(i <- 0 until jsonArr.size()){
    	 locations = Location.makeLocation(jsonArr.get(i).asObject(), this) :: locations
     }
     locations
  }
  
  def post(parameters: ArrayList[NameValuePair], url: String): String = {
    var post = new HttpPost(url)
    post.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"))
    breakResponse(this.execute(post))
  }
  
  def get(url: String): String = {
    var get = new HttpGet(url)
    breakResponse(this.execute(get))
  }
  
   def breakResponse(r: HttpResponse): String = {
    val items = IOUtils.toString(r.getEntity().getContent())
    EntityUtils.consumeQuietly(r.getEntity)
    items
  }
  
}

object SprinklerHTTPClient {
   def LOGIN = "https://cloud.irrigationcaddy.com/auth/login"
   def ACCOUNT = "https://cloud.irrigationcaddy.com/account/accounts"
   def LOCATIONS = "https://cloud.irrigationcaddy.com/location/locations"
   def CONTROLLERS = "https://cloud.irrigationcaddy.com/controller/controllers"
   def GET_STATUS = "https://cloud.irrigationcaddy.com/controller/getControllerStatus/" 
   def GET_PROGRAM = "https://cloud.irrigationcaddy.com/controller/getProgram/"
}
