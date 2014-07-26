package controllers

import play.api.mvc.{Action, Controller}
import model.SprinklerHTTPClient
import org.apache.commons.io.IOUtils
import java.util.Arrays
import org.apache.http.util.EntityUtils
import play.modules.reactivemongo.{ MongoController, ReactiveMongoPlugin }

import reactivemongo.api.gridfs.GridFS
import reactivemongo.api.gridfs.Implicits.DefaultReadFileReader
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson._

object Application extends Controller with MongoController{
  
  val login = new SprinklerHTTPClient("Kevin087", "oliver0564")
  
  def index = Action {
    val collection = db[BSONCollection]("logins")
    login.login
    var locations = List[String]()
    login.locations.foreach(loc => locations = loc.name.toUpperCase() :: locations)
    Ok(views.html.home(login.controllers, locations, login.running, login.notRunning))
  }
}