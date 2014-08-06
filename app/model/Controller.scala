package model

import com.eclipsesource.json.JsonObject
import com.eclipsesource.json.JsonArray
import java.util.Calendar

case class Controller(id: String, name: String, locName: String, locID: String, allowRun: Boolean, running: Boolean, programNumber: Int, flag1: String, flag2: String){
  
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
		val date = secondResponse.get("dateTime").asObject()
		programs = getProgram("3", id, client) :: programs
		programs = getProgram("2", id, client) :: programs
		programs = getProgram("1", id, client) :: programs
		new Controller(id, name, locName, locID, allowRun, running, programNumber, findIcon1(programs, date), findIcon2(-1, client.flow))
	}
	
	def getProgram(i: String, id: String, client: SprinklerHTTPClient): Program = {
	  Program.makeProgram(JsonObject.readFrom(client.get(SprinklerHTTPClient.GET_PROGRAM + id + "/" + i)))
	}
	
	def findIcon1(programs: List[Program], time: JsonObject): String = {
	  val cal = Calendar.getInstance()
	  if(programs(0).allowRun == false && programs(1).allowRun == false && programs(2).allowRun == false){
	    "programsDisabled"
	  }else if(programs(0).timesSet == false && programs(1).timesSet == true && programs(2).timesSet == true){
	    "timesNotSet"
	  }else{
	    if(time.get("hr").asInt != cal.get(Calendar.HOUR_OF_DAY) - 1 || time.get("date").asInt != cal.get(Calendar.DATE) || time.get("month").asInt != (cal.get(Calendar.MONTH) + 1)|| time.get("year").asInt != (cal.get(Calendar.YEAR) - 2000)){
	      "timeIncorrect"
	    }else{
	      "clear"
	    }
	  }
	}
	
	def findIcon2(flow: Int, setFlow: Int): String = {
	  if(flow < 0 || flow == null){
	    "noFlowDetected"
	  }else if(setFlow < flow){
	    "flowAbove"
	  }else{
	    "clear"
	  }
	}
	
}