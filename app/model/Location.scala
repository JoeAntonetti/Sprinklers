package model

import java.util.ArrayList
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair

class Location(args: List[List[String]], client: SprinklerHTTPClient){
  
	val id = args(0)(1).replace("\"", "")
	val name = args.filter(x => x(0).replace("\"", "") == "locationName")(0)(1).replace("\"", "")
	val controllers = getControllers
	
	def getControllers: List[Controller] = {
		var params = new ArrayList[NameValuePair]
        params.add(new BasicNameValuePair("locationId", id))
        params.add(new BasicNameValuePair("dataType", "json"))
        val responseData = client.breakResponse(client.post(params, SprinklerHTTPClient.CONTROLLERS)).grouped(8).toList
        responseData.map(x => new Controller(x, this).fillFromLogin(client))
	}
}