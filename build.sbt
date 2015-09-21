name := """blazon"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

lazy val playSpotify = RootProject(...)

lazy val playSongkick = RootProject(...)

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .dependsOn(playSpotify)
  .dependsOn(playSongkick)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "org.xerial" % "sqlite-jdbc" % "3.7.2"
)
