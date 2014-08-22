import sbt._
import Keys._

object ApplicationBuild extends Build {
  
  val appName = """sprinkler-project"""
  val appVersion = "1.0-SNAPSHOT"

  scalaVersion := "2.10.2"

  val appDependencies = Seq(
    
  )

  val main = play.Project(appName, appVersion, appDependencies);

}
