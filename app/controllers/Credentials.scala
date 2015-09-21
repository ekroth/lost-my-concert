package com.github.ekroth
package controllers

trait ServerCredentials {
  import scala.io.Source

  implicit val spotifyCredentials = spotify.Credentials(
    "http://localhost:9000/authorized",
    Source.fromFile(".spotify_client_id").mkString.replaceAll("\n", ""),
    Source.fromFile(".spotify_client_secret").mkString.replaceAll("\n", ""))

  implicit val songkickCredentials = songkick.Credentials(
    Source.fromFile(".songkick_api_key").mkString.replaceAll("\n", ""))
}
