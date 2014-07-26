package controllers

import play.api.mvc.{Action, Controller}
import model.SprinklerHTTPClient
import org.apache.commons.io.IOUtils
import java.util.Arrays
import org.apache.http.util.EntityUtils

import reactivemongo.api.gridfs.GridFS
import reactivemongo.api.gridfs.Implicits.DefaultReadFileReader
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson._

object Application extends Controller {
  
  val login = new SprinklerHTTPClient("Kevin087", "oliver0564")
  
  def index = Action {
    login.login
    var locations = List[String]()
    login.locations.foreach(loc => locations = loc.name.toUpperCase() :: locations)
    println(locations)
    Ok(views.html.home(login.controllers, locations, login.running, login.notRunning))
  }
}