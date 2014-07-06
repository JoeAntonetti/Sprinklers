package model

class Sprinkler(values: List[List[String]]) {
	val name = values(0)(1).replace("\"", "")
	val allowRun = values.filter(x => x(0).replace("\"", "") == "allowRun")(0)(1)
	val running = values.filter(x => x(0).replace("\"", "") == "running")(0)(1)
	val programNumber = values.filter(x => x(0).replace("\"", "") == "progNumber")(0)(1)
}