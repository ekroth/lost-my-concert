name := """blazon"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

//seq(Revolver.settings: _*)

//lazy val playSpotifyV = "49ed8026289764407aa7b464084d17479e73c8a8"

//lazy val playSpotify = RootProject(uri(s"git://github.com/ekroth/play-spotify#$playSpotifyV"))

lazy val errorhandling = RootProject(uri(s"file:////Users/ekroth/Documents/git/errorhandling"))

lazy val akkaSpotify = RootProject(uri(s"file:////Users/ekroth/Documents/git/play-spotify"))

lazy val akkaSongkick = RootProject(uri(s"file:////Users/ekroth/Documents/git/play-songkick"))

lazy val root = (project in file("."))
  .aggregate(errorhandling, akkaSpotify, akkaSongkick)
  .dependsOn(errorhandling, akkaSpotify, akkaSongkick)

scalaVersion := "2.11.7"

val akkaVersion       = "2.3.14"

val akkaStreamVersion = "1.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %%  "akka-actor"                          % akkaVersion,
  "com.typesafe.akka" %%  "akka-stream-experimental"            % akkaStreamVersion,
  "com.typesafe.akka" %%  "akka-http-core-experimental"         % akkaStreamVersion,
  "com.typesafe.akka" %%  "akka-http-experimental"              % akkaStreamVersion,
  "com.typesafe.akka" %%  "akka-http-spray-json-experimental"   % akkaStreamVersion,
  "com.typesafe.akka" %%  "akka-http-testkit-experimental"      % akkaStreamVersion,
  "org.scalaz"        %% "scalaz-core" % "7.1.4"
)

Revolver.settings
