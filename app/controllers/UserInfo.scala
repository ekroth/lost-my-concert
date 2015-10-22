/* Copyright (c) 2015 Andrée Ekroth.
 * Distributed under the MIT License (MIT).
 * See accompanying file LICENSE or copy at
 * http://opensource.org/licenses/MIT
 */

package com.github.ekroth
package concert
package controllers

import play.api._
import play.api.mvc._

object UserInfo extends Controller with ServerCredentials with spotify.Spotify with spotify.PlayCacheCaching {
  // scalastyle:off public.methods.have.type

  import scala.concurrent._

  import play.api.Play.current
  import play.api.libs.iteratee._
  import play.api.libs.json._
  import play.api.Logger

  import Contexts._
  import spotify._

  private[this] lazy val logger = Logger(getClass())

  def index = Action.async { implicit request =>
    SpotifyAPI.withUserAsync(Unauthorized(views.html.index(None))) { user =>
      SpotifyAPI.currentUserProfile(user).map { profileOpt =>
        Ok(views.html.index(profileOpt))
      }
    }
  }
}
