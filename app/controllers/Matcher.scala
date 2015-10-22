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

object Matcher extends spotify.Spotify with spotify.PlayCacheCaching with songkick.Songkick {
  // scalastyle:off public.methods.have.type

  import scala.collection.immutable.Seq
  import scala.concurrent._
  import scalaz._
  import Scalaz._
  import scalaz.contrib._
  import scalaz.contrib.std._

  import play.api.libs.iteratee._
  import play.api.libs.json._
  import play.api.Logger

  import songkick._
  import errorhandling._

  private[this] lazy val logger = Logger(getClass())

  final def distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double = {
    import scala.math._

    val radius = 6371.0
    val dLat = toRadians(lat2 - lat1)
    val dLon = toRadians(lon2 - lon1)
    val a = sin(dLat / 2) * sin(dLat / 2) + cos(toRadians(lat1)) * cos(toRadians(lat2)) * sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * Math.asin(Math.sqrt(a))
    radius * c
  }

  def retrieveArtist(name: String)(implicit app: Application, ec: ExecutionContext, srv: songkick.Credentials): ResultF[Option[Artist]] = {
    for {
      artistPager <- SongkickAPI.artistSearch(name)
    } yield artistPager.items.headOption
  }

  def retrieveArtistEvents(artist: Artist)(implicit app: Application, ec: ExecutionContext, srv: songkick.Credentials): ResultF[Seq[Event]] = {
    for {
      page <- SongkickAPI.artistEvents(artist.id)
      events <- page.allItems
    } yield events
  }

  def filterDistance(lat: Double, lon: Double, dist: Double)(event: Event): Boolean = {
    val loc = event.location

    if (!(loc.lat.isDefined && loc.lng.isDefined)) {
      logger.warn(s"loc/lat is not defined for event $event")
      false
    } else {
      val diff = distance(lat, lon, loc.lat.get, loc.lng.get)
      diff <= dist
    }
  }

  def relevantEvents(name: String, lat: Double, lon: Double, dist: Double)(implicit app: Application, ec: ExecutionContext, srv: songkick.Credentials): ResultF[Seq[Event]] = {
    logger.info(s"retrieving for $name")
    for {
      artistOpt <- retrieveArtist(name)
      artists = artistOpt.toList
      eventsFs = artists.map(retrieveArtistEvents)
      eventss <- Result.run(eventsFs)
      events = eventss.flatten
      relevant = events.filter(filterDistance(lat, lon, dist))
    } yield relevant
  }


  def relevantEvents(names: Seq[String], lat: Double, lon: Double, dist: Double)(implicit app: Application, ec: ExecutionContext, srv: songkick.Credentials): ResultF[Seq[Event]] = {

    logger.info(names.length.toString)
    logger.info(names.sorted.mkString("\n"))
    val relevantsFs = names.map(name => relevantEvents(name, lat, lon, dist))
    val relevantsF = Result.run(relevantsFs)

    for {
      relevantss <- relevantsF
      relevants = relevantss.flatten
    } yield relevants
  }
}
