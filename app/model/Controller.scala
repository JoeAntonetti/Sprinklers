package model

import com.eclipsesource.json.JsonObject

case class Controller(id: String, name: String, locName: String, locID: String, allowRun: Boolean, running: Boolean, programNumber: Int, programs: List[Program]){
  
}

object Controller{
	
	def makeController(initialResponse: JsonObject, locName: String, locID: String, client: SprinklerHTTPClient): Controller = {
		val id = initialResponse.get("controllerId").asString
		val name = initialResponse.get("controllerName").asString
		val secondResponse = JsonObject.readFrom(client.get(SprinklerHTTPClient.GET_STATUS + id))
		val allowRun = secondResponse.get("allowRun").asBoolean
		val running = secondResponse.get("running").asBoolean
		val programNumber = secondResponse.get("progNumber").asInt
		var programs = List[Program]()
		programs = getProgram("3", id, client) :: programs
		programs = getProgram("2", id, client) :: programs
		programs = getProgram("1", id, client) :: programs		
		new Controller(id, name, locName, locID, allowRun, running, programNumber, programs)
	}
	
	def getProgram(i: String, id: String, client: SprinklerHTTPClient): Program = {
	  Program.makeProgram(JsonObject.readFrom(client.get(SprinklerHTTPClient.GET_PROGRAM + id + "/" + i)))
	}
	
}