package com.joea.sprinklers

import java.util.ArrayList
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import com.joea.sprinklers.SprinklerHTTPClient
import org.apache.commons.io.IOUtils

object Main {
  
  val httpClient = new SprinklerHTTPClient("Kevin087", "oliver0564")
  
  def main(args: Array[String]){
    httpClient.login
    println(IOUtils.toString(httpClient.get(SprinklerHTTPClient.GET_STATUS).getEntity().getContent()))
  }
}