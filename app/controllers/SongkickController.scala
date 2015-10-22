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

object SongkickController extends Controller with ServerCredentials with songkick.Songkick {
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

  /* Search */

  def locationNameSearchAction(query: String) = Action.async {
    SongkickAPI.locationNameSearch(query).run.map(x => Ok(views.html.message(x.toString)))
  }


  /* Calendars */

  def metroEventsAction(id: String) = Action.async {
    SongkickAPI.metroEvents(id).run.map(x => Ok(views.html.message(x.toString)))
  }

  /* Extensions */

  def idsByMetroName(name: String): ResultF[Seq[String]] = {
    for {
      locsPageR <- SongkickAPI.locationNameSearch(name)
      locsR <- locsPageR.allItems
    } yield for {
      loc <- locsR
      id = loc.metroArea.id
    } yield id.toString
  }

  def ids(name: String) = Action.async { implicit request =>
    val q = for {
      ids <- idsByMetroName(name)
      cids <- idsByMetroName("Copenhagen")
    } yield ids ++ cids

    for {
      ids <- q.run
    } yield Ok(views.html.message(ids.toString))
  }


}
