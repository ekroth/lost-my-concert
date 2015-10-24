package com.github.ekroth
package concert

import scala.concurrent._

import akka.actor._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._

object Main extends App with songkick.Songkick with ServerCredentials {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val exec = system.dispatcher

  import scala.concurrent._
  import scalaz._
  import scalaz.contrib._
  import scalaz.contrib.std._

  import errorhandling._

  val route =
    get {
      parameters('artist.as[String]) { artist =>
        complete {
          import Scalaz._
          val theFuck = (for {
            page <- SongkickAPI.artistSearch(artist)
            all <- page.allItems
          } yield all).run

          theFuck.map {
            case x => x.toString
          }
        }
      }
    }

  Http().bindAndHandle(route, "localhost", 8080)
}
