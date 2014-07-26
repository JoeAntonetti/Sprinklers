import sbt._
import Keys._

object ApplicationBuild extends Build {

  val appName = """sprinkler-project"""
  val appVersion = "1.0-SNAPSHOT"

  scalaVersion := "2.10.2"

  val appDependencies = Seq(
    "org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.akka23-SNAPSHOT"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
      // settings
  )

}
