/* Copyright (c) 2015 Andrée Ekroth.
 * Distributed under the MIT License (MIT).
 * See accompanying file LICENSE or copy at
 * http://opensource.org/licenses/MIT
 */

package com.github.ekroth
package controllers

import play.api._
import play.api.mvc._

object Kick extends Controller with ServerCredentials with spotify.Spotify with spotify.PlayCacheCaching with songkick.Songkick {
  // scalastyle:off public.methods.have.type

  import scala.concurrent._

  import play.api.Play.current
  import play.api.libs.iteratee._
  import play.api.libs.json._
  import play.api.Logger

  import Contexts._
  import spotify._

  private[this] lazy val logger = Logger(getClass())

  def idsByMetroName(name: String): Future[Seq[String]] = {
    for {
      locsPage <- locationNameSearch(name)
      locs <- locsPage.allItems
    } yield for {
      loc <- locs
      id <- loc \\ "id"
    } yield id.toString
  }

  def venuesByMetroName(name: String): Future[Seq[JsValue]] = {
    for {
      ids <- idsByMetroName(name)
      eventsF = ids.map(metroEvents)
      event <- Future.sequence(eventsF)
      pagesF = event.map(_.allItems)
      allEvents <- Future.sequence(pagesF)
    } yield allEvents.flatten
  }

  def venues(name: String) = Action.async { implicit request =>
    for {
      venues <- venuesByMetroName(name)
      pretty = venues.map(Json.prettyPrint)
    } yield Ok(views.html.message(pretty.mkString("\nxxxxxxxx\n")))
  }

  // def search(query: String) = Action.async { implicit request =>
  //   for {
  //     pageOpt <- artistSearch(query)
  //     items <- pageOpt.map(_.allItems).getOrElse(Future.successful(Seq.empty))
  //   } yield {
  //     Ok(views.html.message(items.mkString("\n")))
  //   }
  // }

  // def locationName(name: String) = Action.async { implicit request =>
  //   for {
  //     pageOpt <- locationNameSearch(name)
  //     items <- pageOpt.map(_.allItems).getOrElse(Future.successful(Seq.empty))
  //   } yield {
  //     Ok(views.html.message(items.mkString("\n")))
  //   }
  // }

  // def metro(id: String) = Action.async { implicit request =>
  //   for {
  //     pageOpt <- metroEvents(id)
  //     items <- pageOpt.map(_.allItems).getOrElse(Future.successful(Seq.empty))
  //   } yield {
  //     Ok(views.html.message(items.mkString("\n")))
  //   }
  // }

  // def calendarMetro(name: String) = Action.async { implicit request =>
  //   val ids
  //   val idFut = locationNameSearch(name).map { x =>
  //     x.flatMap(y => (y.underlying.results.head._2.head \\ "id").headOption).head
  //   }

  //   for {
  //     idOpt <- idFut
  //     pageOpt <- metroEvents(idOpt.toString)
  //     items <- pageOpt.map(_.allItems).getOrElse(Future.successful(Seq.empty))
  //   } yield {
  //     Ok(views.html.message(items.mkString("\n")))
  //   }
  // }
}
