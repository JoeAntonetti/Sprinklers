package models

import com.eclipsesource.json.JsonObject

case class Controller(id: String, name: String, locName: String, locID: String, allowRun: Boolean, running: Boolean, programNumber: Int){
  
}

object Controller{
	
	def makeController(initialResponse: JsonObject, locName: String, locID: String, secondResponse: JsonObject): Controller = {
		val id = initialResponse.get("controllerId").asString
		val name = initialResponse.get("controllerName").asString
		val allowRun = secondResponse.get("allowRun").asBoolean
		val running = secondResponse.get("running").asBoolean
		val programNumber = secondResponse.get("progNumber").asInt
		new Controller(id, name, locName, locID, allowRun, running, programNumber)
	}
	
}