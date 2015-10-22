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

object BandsintownController extends Controller with ServerCredentials with bandsintown.Bandsintown {
  // scalastyle:off public.methods.have.type

  import scala.concurrent._
  import scalaz._
  import Scalaz._
  import scalaz.contrib._
  import scalaz.contrib.std._

  import play.api.Play.current
  import play.api.libs.iteratee._
  import play.api.libs.json._
  import play.api.Logger

  import Contexts._
  import errorhandling._

  private[this] lazy val logger = Logger(getClass())


  def findArtistAction(artist: String) = Action.async { implicit request =>
    for {
      artist <- BandsintownAPI.findArtist(artist).run
    } yield Ok(views.html.message(artist.toString))
  }

  def findEventsAction(artist: String) = Action.async { implicit request =>
    for {
      events <- BandsintownAPI.findEvents(artist).run
    } yield Ok(views.html.message(events.toString))
  }

  def searchEventsAction(q: String, location: String) = Action.async { implicit request =>
    for {
      events <- BandsintownAPI.searchEvents(q, location, 30).run
    } yield Ok(views.html.message(events.toString))
  }

  def searchRecommendedAction(q: String, location: String, o: Boolean) = Action.async { implicit request =>
    for {
      events <- BandsintownAPI.searchRecommended(q, location, 30, o).run
    } yield Ok(views.html.message(events.toString))
  }

}
