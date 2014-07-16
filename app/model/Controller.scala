package model

import com.eclipsesource.json.JsonObject

case class Controller(id: String, name: String, locName: String, locID: String, allowRun: Boolean, running: Boolean, programNumber: Int){
  
}

object Controller{
	
	def makeController(initialResponse: JsonObject, locName: String, locID: String, client: SprinklerHTTPClient): Controller = {
		val id = initialResponse.get("controllerId").asString
		val name = initialResponse.get("controllerName").asString
		val secondResponse = JsonObject.readFrom(client.getString(SprinklerHTTPClient.GET_STATUS + id))
		val allowRun = secondResponse.get("allowRun").asBoolean()
		val running = secondResponse.get("running").asBoolean()
		println(secondResponse.get("running").toString())
		val programNumber = secondResponse.get("progNumber").asInt()
		val controller = new Controller(id, name, locName, locID, allowRun, running, programNumber)
		if(running == true){
	  	  client.running = client.running + 1
	  	  client.runningControllers = controller :: client.runningControllers
	  	}else{
	  	  client.notRunning = client.notRunning + 1
	  	  client.notRunningControllers = controller :: client.notRunningControllers
	  	}
		controller
	}
	
}