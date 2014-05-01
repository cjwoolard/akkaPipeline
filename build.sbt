name := "ciqMessagingPoC"

version := "1.0"

scalaVersion := "2.10.4"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Scala-tools" at "https://oss.sonatype.org/content/groups/scala-tools"

resolvers += "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

//conflictManager := ConflictManager.strict

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-contrib" % "2.2.1",
  "com.typesafe.akka" %% "akka-actor" % "2.2.1",
  "com.typesafe.akka" %% "akka-testkit" % "2.2.1",
  "com.typesafe.akka" %% "akka-slf4j" % "2.2.1",
  "com.typesafe.atmos" % "trace-akka-2.1.4" % "1.3.1",
  "org.mongodb" % "casbah_2.10" % "2.5.1",
  "com.novus" % "salat_2.10" % "1.9.7-SNAPSHOT",
  "org.slf4j" % "slf4j-api" % "1.7.7",
  "org.slf4j" % "slf4j-simple" % "1.7.7",
  "org.scalatest" % "scalatest_2.10" % "2.1.0" % "test",
  "org.scalaz" %% "scalaz-core" % "7.0.6"
)

atmosSettings

com.typesafe.sbt.SbtAtmos.traceAkka("2.2.1")