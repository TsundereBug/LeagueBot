name := "LeagueBot"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "com.github.austinv11" % "Discord4J" % "00d4d3d2e5",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.google.code.gson" % "gson" % "2.8.1"
)

resolvers += "jcenter" at "http://jcenter.bintray.com"
resolvers += "jitpack.io" at "https://jitpack.io"