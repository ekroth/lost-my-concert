/* Copyright (c) 2015 Andrée Ekroth.
 * Distributed under the MIT License (MIT).
 * See accompanying file LICENSE or copy at
 * http://opensource.org/licenses/MIT
 */

package com.github.ekroth
package concert
package interpreters

import scala.concurrent._

import scalaz._
import Scalaz._

class Akka(implicit ec: ExecutionContext, sys: akka.actor.ActorSystem,
  mat: akka.stream.Materializer, srv: songkick.Credentials)
    extends Interpreter with songkick.Songkick {

  import songkick._

  import api._
  import api.SongkickOps._

  import errorhandling._

  override def trans = new (Op ~> ResultF) {
    override def apply[A](op: Op[A]): ResultF[A] = op match {

      case ArtistSearch(artist) =>
        for {
          page <- SongkickAPI.artistSearch(artist)
          all <- page.allItems
        } yield all

      case ArtistEvents(id) => {
        for {
          page <- SongkickAPI.artistEvents(id)
          all <- page.allItems
        } yield all

      }
    }
  }
}
