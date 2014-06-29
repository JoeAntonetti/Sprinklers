package com.joea.sprinklers

import org.apache.http.impl.client.DefaultHttpClient
import java.util.ArrayList
import org.apache.http.NameValuePair
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.util.EntityUtils
import org.apache.http.message.BasicNameValuePair

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
  
  def get(url: String): HttpResponse = {
    var get = new HttpGet(url)
    this.execute(get)
  }
  
}

object SprinklerHTTPClient {
   def LOGIN = "https://cloud.irrigationcaddy.com/auth/login"
   def GET_STATUS = "https://cloud.irrigationcaddy.com/controller/settings/17455?_=1404055309702"
}
