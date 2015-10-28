/* Copyright (c) 2015 Andrée Ekroth.
 * Distributed under the MIT License (MIT).
 * See accompanying file LICENSE or copy at
 * http://opensource.org/licenses/MIT
 */

package com.github.ekroth
package concert

import scala.concurrent._

import akka.actor._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._

object Main extends App with api.Songkick with ServerCredentials {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val exec = system.dispatcher

  import scala.concurrent._
  import scalaz._
  import scalaz.std.AllInstances._
  import scalaz.contrib._
  import scalaz.contrib.std._

  import errorhandling._
  import interpreters._

  def getEvents(artist: String) =
    for {
      artists <- SongkickAPI.artistSearch(artist)
      events <- SongkickAPI.artistEvents(artists.head.id)
    } yield events

  val route =
    get {
      pathPrefix("broken") {
        parameters('artist.as[String]) { artist =>
          complete {
            val theFuck = Free.runFC(getEvents(artist))((new Broken() with Logging).trans).run
            theFuck.map {
              case x => x.toString
            }
          }
        }
      }  ~
      pathPrefix("pure") {
        parameters('artist.as[String]) { artist =>
          complete {
            val theFuck = Free.runFC(getEvents(artist))((new Mock() with Logging).trans).run
            theFuck.map {
              case x => x.toString
            }
          }
        }
      }  ~
      pathPrefix("real") {
        parameters('artist.as[String]) { artist =>
          complete {
            val theFuck = Free.runFC(getEvents(artist))((new Akka() with Logging).trans).run
            theFuck.map {
              case x => x.toString
            }
          }
        }
      }
    }

  Http().bindAndHandle(route, "localhost", 8080)
}
