package model

import java.util.ArrayList
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import com.eclipsesource.json.JsonObject
import com.eclipsesource.json.JsonArray
import com.eclipsesource.json.JsonValue

class Location(args: List[List[String]], client: SprinklerHTTPClient){
  
	val id = args(0)(1).replace("\"", "")
	val name = args.filter(x => x(0).replace("\"", "") == "locationName")(0)(1).replace("\"", "")
	val controllers = getControllers
	
	def getControllers: List[Controller] = {
		var params = new ArrayList[NameValuePair]
        params.add(new BasicNameValuePair("locationId", id))
        params.add(new BasicNameValuePair("dataType", "json"))
        val responseData = client.stringResponse(client.post(params, SprinklerHTTPClient.CONTROLLERS))
        val jsonArr = JsonArray.readFrom(responseData)
        var controllers = List[Controller]()
        for(i <- 0 until jsonArr.size()){
          controllers = Controller.makeController(jsonArr.get(i).asObject(), this.name, this.id, client) :: controllers
        }
		controllers
	}
}