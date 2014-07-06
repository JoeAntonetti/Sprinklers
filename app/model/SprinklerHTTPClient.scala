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
  
  def login = {
     var params = new ArrayList[NameValuePair]
     params.add(new BasicNameValuePair("login", username));
     params.add(new BasicNameValuePair("password", password));  
     EntityUtils.consumeQuietly(this.post(params, SprinklerHTTPClient.LOGIN).getEntity)
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
   def GET_STATUS = "https://cloud.irrigationcaddy.com/controller/getControllerStatus/17455?time=1402194931222"
     
   def createSprinkler(login: List[String]): Sprinkler = {
    val httpClient = new SprinklerHTTPClient(login(0), login(1))
    httpClient.login
    val response = httpClient.get(SprinklerHTTPClient.GET_STATUS)
    new Sprinkler(response)
   }
   
}
