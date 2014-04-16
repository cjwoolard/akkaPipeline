name := "ciqMessagingPoC"

version := "1.0"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-contrib" % "2.2.1",
  "com.typesafe.akka" %% "akka-actor" % "2.2.1",
  "com.typesafe.akka" %% "akka-testkit" % "2.2.1",
  "org.mongodb" %% "casbah" % "2.7.0",
  "org.slf4j" % "slf4j-api" % "1.7.7",
  "org.slf4j" % "slf4j-simple" % "1.7.7"
)