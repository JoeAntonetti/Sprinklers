package model

class Controller(args: List[List[String]], location: Location){
  
	val id = args(0)(1).replace("\"", "")
	val name = args.filter(x => x(0).replace("\"", "") == "controllerName")(0)(1).replace("\"", "")
	val locName = location.name
	val locID = location.id
	var allowRun: String = _
	var running: String = _
	var programNumber: String = _
	
	def fillFromLogin(client: SprinklerHTTPClient): Controller = {
	    val values = client.get(SprinklerHTTPClient.GET_STATUS + id)
	  	allowRun = values.filter(x => x(0).replace("\"", "") == "allowRun")(0)(1)
	  	running = values.filter(x => x(0).replace("\"", "") == "running")(0)(1)
	  	programNumber = values.filter(x => x(0).replace("\"", "") == "progNumber")(0)(1)
	  	this
	}
	
}