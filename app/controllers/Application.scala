package controllers

import scala.io.Source
import model.SprinklerHTTPClient
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.mvc.Result
import scalax.io.Codec
import scalax.io.Output
import scalax.io.Resource
import java.util.Calendar
import com.sun.net.ssl.SSLContext
import com.sun.net.ssl.TrustManager
import com.sun.net.ssl.X509TrustManager
import java.security.cert.X509Certificate
import org.apache.http.conn.ClientConnectionManager
import org.apache.http.conn.scheme.SchemeRegistry
import org.apache.http.conn.scheme.Scheme
import java.security.SecureRandom
import org.apache.http.conn.ssl.SSLSocketFactory
import java.io.File



object Application extends Controller{
  
  var logins = List[SprinklerHTTPClient]()
  var controllers = List[model.Controller]()
  var running = 0
  var notRunning = 0
  var locations = List[String]()
  var firstTime = true

  
   val loginForm = Form(
        tuple(
             "username" -> text,
             "password" -> text
        )
    )
  
  def index = Action {
    //login.login
    read("logins")
    if(firstTime == true){
    	for(login <- logins){
    		println(login)
    		login.login
    		controllers = login.controllers ++ controllers
      	login.locations.foreach(loc => locations = loc.name.toUpperCase() :: locations)
    	}
    	firstTime = false
    }
    Ok(views.html.home(controllers, locations, running, notRunning))
  }
  
  def write(s1: String, s2: String, s3: String, f: String) = {
    val output:Output = Resource.fromFile("data\\" + f)
    output.writeStrings(List(s1,s2,s3),"|")(Codec.UTF8)
    output.write("\n")
  }
  
  def addLogin = Action { implicit request =>
    val (username, password) = loginForm.bindFromRequest.get
    write(username, password, "0", "logins")
    //read("logins")
  // for(login <- logins){
    //  login.login
    // controllers = login.controllers ++ controllers
    //  login.locations.foreach(loc => locations = loc.name.toUpperCase() :: locations)
    //}
    var l = new SprinklerHTTPClient(username, password, "0")
    logins = l :: logins
    l.login
    controllers = l.controllers ++ controllers
    //logins = List[SprinklerHTTPClient]()
    //controllers = List[model.Controller]()
   // firstTime = true
    Ok(views.html.home(controllers, locations, running, notRunning))
  }
  
  def read(f: String) = {
    var file = new File("data\\" + f)
    if(!file.exists()){
      var b = new File("data")
      b.mkdirs()
      file.createNewFile()
      write("Kevin087", "oliver0564", "4", f)
    }    
    for (line <- Source.fromFile("data\\" + f).getLines()) {
    	var loginData = line.split('|')
    	println(loginData)
    	logins = new SprinklerHTTPClient(loginData(0), loginData(1), "0") :: logins
    }
  }
}