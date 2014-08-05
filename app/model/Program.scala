package model

import com.eclipsesource.json.JsonObject
import com.eclipsesource.json.JsonArray

case class Program(allowRun: Boolean, timesSet: Boolean){

}

object Program{
  
  def makeProgram(response: JsonObject): Program = {
    val startTimes = response.get("startTimes").asArray()
    var timesSet = false
    for(i <- 0 until startTimes.size()){
    	val obj = startTimes.get(i).asObject
    	if(obj.get("hr").asInt > 0 && obj.get("min").asInt > 0){
    	  timesSet = true
    	}
    }
    new Program(response.get("allowRun").asBoolean, timesSet)
  }
}