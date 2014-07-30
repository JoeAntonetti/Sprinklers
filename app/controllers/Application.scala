package controllers

import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import model.SprinklerHTTPClient
import play.api.mvc.Action
import play.api.mvc.Controller
import java.io.FileWriter
import java.io.File
import scalax.io.Resource
import scalax.io.Output
import scalax.io.Codec
import scala.io.Source

object Application extends Controller{
  
  val login = new SprinklerHTTPClient("Kevin087", "oliver0564")
  var logins = List[SprinklerHTTPClient]()
  var controllers = List[model.Controller]()
  var running = 0
  var notRunning = 0
  var locations = List[String]()
  
  def index = Action {
    //login.login
    read("logins")
    for(login <- logins){
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
  
  def read(f: String) = {
    for (line <- Source.fromFile("data\\" + f).getLines()) {
    	var loginData = line.split('|')
    	logins = new SprinklerHTTPClient(loginData(0), loginData(1)) :: logins
    }
  }
}