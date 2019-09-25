name := "crud-simpe-akka-http-circe-seguros"

version := "0.1"

scalaVersion := "2.13.1"
val circeVersion = "0.12.1"

libraryDependencies ++= Seq(
"com.typesafe.akka" %% "akka-http"   % "10.1.10",
"com.typesafe.akka" %% "akka-stream" % "2.5.23",

  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "de.heikoseeberger" %% "akka-http-circe" % "1.29.1",
  "org.reactivemongo" %% "reactivemongo" % "0.18.6",
  "io.monix" %% "monix" % "3.0.0"
)
