/* Copyright (c) 2015 Andrée Ekroth.
 * Distributed under the MIT License (MIT).
 * See accompanying file LICENSE or copy at
 * http://opensource.org/licenses/MIT
 */

package com.github.ekroth
package concert

trait ServerCredentials {
  import scala.io.Source

  implicit val spotifyCredentials = spotify.Credentials(
    "http://localhost:9000/authorized",
    Source.fromFile(".spotify_client_id").mkString.replaceAll("\n", ""),
    Source.fromFile(".spotify_client_secret").mkString.replaceAll("\n", ""))

  implicit val songkickCredentials = songkick.Credentials(
    Source.fromFile(".songkick_api_key").mkString.replaceAll("\n", ""))
}
