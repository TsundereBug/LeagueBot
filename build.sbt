name := "LeagueBot"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "com.github.austinv11" % "Discord4J" % "dev-SNAPSHOT",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

resolvers += "jcenter" at "http://jcenter.bintray.com"
resolvers += "jitpack.io" at "https://jitpack.io"