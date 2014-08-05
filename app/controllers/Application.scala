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

object Application extends Controller{
  
  val login = new SprinklerHTTPClient("Kevin087", "oliver0564")
  var logins = List[SprinklerHTTPClient]()
  var controllers = List[model.Controller]()
  var running = 0
  var notRunning = 0
  var locations = List[String]()
  
   val loginForm = Form(
        tuple(
             "username" -> text,
             "password" -> text
        )
    )
  
  def index = Action {
    //login.login
    read("logins")
    for(login <- logins){
      println(login)
      login.login
      controllers = login.controllers ++ controllers
      login.locations.foreach(loc => locations = loc.name.toUpperCase() :: locations)
    }
    Ok(views.html.home(controllers, locations, running, notRunning))
  }
  
  def write(s1: String, s2: String, f: String) = {
    val output:Output = Resource.fromFile("data\\" + f)
    output.writeStrings(List(s1,s2),"|")(Codec.UTF8)
    output.write("\n")
  }
  
  def addLogin = Action { implicit request =>
    val (username, password) = loginForm.bindFromRequest.get
    write(username, password, "logins")
    read("logins")
    for(login <- logins){
      login.login
      controllers = login.controllers ++ controllers
      login.locations.foreach(loc => locations = loc.name.toUpperCase() :: locations)
    }
    Ok(views.html.home(controllers, locations, running, notRunning))
  }
  
  def read(f: String) = {
    for (line <- Source.fromFile("data\\" + f).getLines()) {
    	var loginData = line.split('|')
    	println(loginData)
    	logins = new SprinklerHTTPClient(loginData(0), loginData(1)) :: logins
    }
  }
}