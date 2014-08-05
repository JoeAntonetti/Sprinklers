package model

import com.eclipsesource.json.JsonObject

case class Program(allowRun: Boolean){

}

object Program{
  
  def makeProgram(response: JsonObject): Program = {
    new Program(response.get("allowRun").asBoolean)
  }
}