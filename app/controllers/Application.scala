package controllers

import play.api.mvc.{Action, Controller}
import model.SprinklerHTTPClient
import org.apache.commons.io.IOUtils
import java.util.Arrays
import org.apache.http.util.EntityUtils

object Application extends Controller {
  
  val login = new SprinklerHTTPClient("Kevin087", "oliver0564")
  
  def index = Action {
    login.login
    Ok(views.html.home(login.controllers))
  }
}