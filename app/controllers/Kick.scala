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

object Kick extends Controller with ServerCredentials with spotify.Spotify with spotify.PlayCacheCaching {
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
  import spotify._
  import songkick._
  import errorhandling._

  private[this] lazy val logger = Logger(getClass())

  def relevant(lat: Double, lon: Double, dist: Double) = Action.async { implicit request =>
    SpotifyAPI.withUserAsync(Ok(views.html.index(None))) { user =>
      (for {
        artistPage <- SpotifyAPI.currentUserFollowedArtists(user)
        artists <- artistPage.allItems(user)
        names = artists.map(_.name)
        events <- Matcher.relevantEvents(names, lat, lon, dist)
        eventNames = events.map(_.displayName)
      } yield eventNames).run.map {
        case \/-(xs) => Ok(views.html.message(xs.mkString("\n")))
        case -\/(x) => Ok(views.html.message(x.toString))
      }
    }
  }



/*
  def eventsByMetroName(name: String): ResultF[Seq[Event]] = {
    for {
      ids <- idsByMetroName(name)
      eventsF = ids.map(metroEvents)
      event <- ResultFOK(Future.sequence(eventsF).right)
      pagesF = event.map(_.allItems)
      allEvents <- ResultFOK(Future.sequence(pagesF))
    } yield allEvents.flatten
  }

  def eventsToArtistNames(xs: Seq[Event]): Seq[String] =
    for {
      x <- xs
      p <- x.performance
      as = p.artist
      name = as.displayName
      n = name.asInstanceOf[JsString]
    } yield n.value

  def userFollowedArtists = Action.async { implicit request =>
    withUserAsync(Ok(views.html.message("Who are you really?"))) { user =>
      for {
        artistsOpt <- currentUserFollowedArtists(user)
        artists <- artistsOpt.map(_.allItems(user)).getOrElse(Future.successful(Nil))
        names = artists.map(_.name)
        } yield Ok(views.html.message(names.mkString(", ")))
      }
    }

  def userLiked = Action.async { implicit request =>
    withUserAsync(Ok(views.html.message("Who are you really?"))) { user =>
      for {
        tracksOpt <- currentUserTracks(user)
        tracks <- tracksOpt.map(_.allItems(user)).getOrElse(Future.successful(Nil))
        track = tracks.map(_.track.name)
        } yield Ok(views.html.message(track.mkString(", ")))
      }
    }

  def search(query: String) = Action.async { implicit request =>
    withUserAsync(Ok(views.html.message("I don't really trust you, user."))) { user =>
      withClientAsync(Ok(views.html.message("Stop it client, stop it!"))) { client =>
        for {
          artistPageOpt <- searchArtist(client, query)
          allOptF = artistPageOpt.map(_.allItems(client))
          all <- allOptF.getOrElse(Future.successful(Seq.empty))
          ids = all.map(_.id)
          names = all.map(_.name)
          follows <- currentUserIsFollowing(user, ids: _*)
          fWithName = follows.zip(names)
        } yield Ok(views.html.message(fWithName.mkString(", ")))
      }
    }
  }

  def around(metroName: String) = Action.async { implicit request =>
    withUserAsync(Ok(views.html.message("Who are you really?"))) { user =>
      for {
        events <- eventsByMetroName(metroName)
        artistNames = eventsToArtistNames(events)
        eventNames = events.map(_.displayName)
        artistsOpt <- currentUserFollowedArtists(user)
        artists <- artistsOpt.map(_.allItems(user)).getOrElse(Future.successful(Nil))
        names = artists.map(_.name.toLowerCase).toSet
        nameWithEvents = eventNames.zip(artistNames)
        matches = nameWithEvents.filter(x => names.contains(x._2.toLowerCase))
        matchNames = matches.map(_._1)
      } yield {
//        logger.debug(s"Venues: $venues")
//        logger.debug(s"VenueNames: $venueNames")
        logger.debug(s"Available: $artistNames")
        logger.debug(s"Following: $names")
        logger.debug(s"Matches: $matches")
        Ok(views.html.message(matchNames.mkString(", ")))
      }
    }
  }

  def venues(name: String) = Action.async { implicit request =>
    for {
      events <- eventsByMetroName(name)
    } yield Ok(views.html.message(events.mkString(", ")))
  }

  def artists(name: String) = Action.async { implicit request =>
    for {
      events <- eventsByMetroName(name)
      names = eventsToArtistNames(events)
    } yield Ok(views.html.message(names.mkString(", ")))
  }
 */

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
