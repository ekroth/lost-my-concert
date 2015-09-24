name := """blazon"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

//seq(Revolver.settings: _*)

//lazy val playSpotifyV = "49ed8026289764407aa7b464084d17479e73c8a8"

//lazy val playSpotify = RootProject(uri(s"git://github.com/ekroth/play-spotify#$playSpotifyV"))

lazy val playSpotify = RootProject(uri(s"file:////Users/ekroth/Documents/git/play-spotify"))

lazy val playSongkick = RootProject(uri(s"file:////Users/ekroth/Documents/git/play-songkick"))

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .aggregate(playSpotify, playSongkick)
  .dependsOn(playSpotify, playSongkick)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "org.xerial" % "sqlite-jdbc" % "3.7.2"
)
