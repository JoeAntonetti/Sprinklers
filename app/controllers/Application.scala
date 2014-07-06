package controllers

import play.api.mvc.{Action, Controller}
import model.SprinklerHTTPClient
import org.apache.commons.io.IOUtils
import java.util.Arrays
import org.apache.http.util.EntityUtils
import model.Sprinkler

object Application extends Controller {
  
  val logins = List(List("Kevin087", "oliver0564"), List("Kevin087", "oliver0564"))
  
  def index = Action {
    val clients = logins.map(login => SprinklerHTTPClient.createSprinkler(login))
    Ok(views.html.index(clients))
  }
}