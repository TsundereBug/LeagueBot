name := "LeagueBot"

version := "1.0"

scalaVersion := "2.12.2"

mainClass in assembly := Some("com.tsunderebug.leaguebot.Main")

libraryDependencies ++= Seq(
  "com.github.austinv11" % "Discord4J" % "6399aa0573",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.google.code.gson" % "gson" % "2.8.1"
)

resolvers += "jcenter" at "http://jcenter.bintray.com"
resolvers += "jitpack.io" at "https://jitpack.io"