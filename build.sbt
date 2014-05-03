name := "ciqMessagingPoC"

version := "1.0"

scalaVersion := "2.10.4"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-contrib" % "2.2.1",
  "com.typesafe.akka" %% "akka-actor" % "2.2.1",
  "com.typesafe.akka" %% "akka-testkit" % "2.2.1",
  "com.typesafe.akka" %% "akka-slf4j" % "2.2.1",
  "com.typesafe.atmos" % "trace-akka-2.1.4" % "1.3.1",
  "com.novus" % "salat_2.10" % "1.9.7",
  "org.slf4j" % "slf4j-api" % "1.7.7",
  "org.slf4j" % "slf4j-simple" % "1.7.7",
  "org.scalatest" % "scalatest_2.10" % "2.1.0" % "test",
  "org.scalaz" %% "scalaz-core" % "7.0.6"
)