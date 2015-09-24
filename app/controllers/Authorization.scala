/* Copyright (c) 2015 Andrée Ekroth.
 * Distributed under the MIT License (MIT).
 * See accompanying file LICENSE or copy at
 * http://opensource.org/licenses/MIT
 */

package com.github.ekroth
package controllers

import play.api._
import play.api.mvc._

object Authorization extends Controller with ServerCredentials with spotify.Spotify with spotify.PlayCacheCaching {
  import scala.concurrent.Future

  import play.api.Play.current

  import Contexts.execContext

  val rand = new scala.util.Random()

  val stateLength = 16

  /** Redirect user to Spotify authorization.
    *
    * A random state is generated and added to the session cookie.
    * Spotify redirects back to `authorized`.
    */
  def authorize = Action {
    val state = rand.alphanumeric.take(stateLength).mkString
    Redirect(redirectUri(Some(state))).withSession("state" -> state)
  }

  /** Redirect from Spotify authorization. Obtain access token.
    *
    * The state variable in the session is required to be equal to the
    * state parameter supplied by Spotify.
    * If the state is correct, it will try to obtain the access token
    * using `spotify.Commands.userAuth`.
    */
  def authorized = Action.async { request =>
    val ps = request.queryString
    val state = ps.get("state").map(_.mkString)
    val expectedState = request.session.get("state")
    val unable = Unauthorized(views.html.message("Unable to authorize."))

    if (state.isDefined && expectedState.isDefined && state.get == expectedState.get) {
      (ps.get("code"), ps.get("error")) match {

        case (Some(c), None) => userAuth(c.mkString).map { userOpt =>
          userOpt.map(_ => Redirect("/").withSession("auth_code" -> c.mkString)).getOrElse(unable)
        }

        case (None, Some(e)) => Future.successful(unable)
        case _ => Future.successful(unable)
      }
    } else {
      Future.successful(unable)
    }
  }
}
