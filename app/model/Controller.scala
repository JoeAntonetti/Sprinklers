package model

import com.eclipsesource.json.JsonObject
import com.eclipsesource.json.JsonArray
import java.util.Calendar

case class Controller(id: String, name: String, locName: String, locID: String, uname: String, pword: String, allowRun: Boolean, running: Boolean, programNumber: Int, flag1: String, flag2: String){
  
}

object Controller{
	
	def makeController(initialResponse: JsonObject, locName: String, locID: String, client: SprinklerHTTPClient): Controller = {
		val id = initialResponse.get("controllerId").asString
		val name = initialResponse.get("controllerName").asString
		val secondResponse = JsonObject.readFrom(client.get(SprinklerHTTPClient.GET_STATUS + id))
		var allowRun = if(secondResponse.get("allowRun") != null) secondResponse.get("allowRun").asBoolean else false
		var running = if(secondResponse.get("running") != null) secondResponse.get("running").asBoolean else false
		val programNumber = if(secondResponse.get("progNumber") != null) secondResponse.get("progNumber").asInt else 0
		var programs = List[Program]()
		val date = if(secondResponse.get("dateTime") != null) secondResponse.get("dateTime").asObject() else null
		programs = getProgram("3", id, client) :: programs
		programs = getProgram("2", id, client) :: programs
		programs = getProgram("1", id, client) :: programs
		new Controller(id, name, locName, locID, client.uname, client.pword, allowRun, running, programNumber, findIcon1(programs, date), findIcon2(-1, client.flow))
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
	    }else if(time.get("min").asInt < (cal.get(Calendar.MINUTE) + 5) && time.get("min").asInt > (cal.get(Calendar.MINUTE) - 5)){
	      "clear"
	    }else{
	      "timeIncorrect"
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