package models

import java.util.ArrayList
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import com.eclipsesource.json.JsonObject
import com.eclipsesource.json.JsonArray
import com.eclipsesource.json.JsonValue



case class Location(id: String, name: String, controllers: List[Controller])

object Location{
 
	def makeLocation(args: JsonObject, client: SprinklerHTTPClient): Location = {
		val id = args.get("locationId").asString()
	    val name = args.get("locationName").asString
		var params = new ArrayList[NameValuePair]
        params.add(new BasicNameValuePair("locationId", id))
        params.add(new BasicNameValuePair("dataType", "json"))
        val responseData = client.post(params, SprinklerHTTPClient.CONTROLLERS)
        val jsonArr = JsonArray.readFrom(responseData)
        var controllers = List[Controller]()
        for(i <- 0 until jsonArr.size()){
          controllers = Controller.makeController(jsonArr.get(i).asObject(), name, id, JsonObject.readFrom(client.get(SprinklerHTTPClient.GET_STATUS + jsonArr.get(i).asObject().get("controllerId").asString()))) :: controllers
        }
		new Location(id, name, controllers)
	}
}