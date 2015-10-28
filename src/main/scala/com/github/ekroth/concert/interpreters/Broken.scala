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

class Broken(implicit ec: ExecutionContext) extends Interpreter {

  import songkick._

  import api._
  import api.SongkickOps._

  import errorhandling._

  override def trans = new (Op ~> ResultF) {

    override def apply[A](op: Op[A]): ResultF[A] = op match {

      case ArtistSearch(artist) => Result.failF(SongkickError.Usage("BROKEN"))

      case ArtistEvents(id) => Result.okF(Seq(Event(
        1337, "Concert", "waat", "Roooskilde",
        Date(None, "Odd-Time", None),
        Location("Sweden", Some(11.6), Some(1.2)),
        Venue(None, "Malmöfestivalen", None, Some(11.7), Some(1.25), MetroArea("lol", "Malmö", DisplayName("Malmö"), 17, Some(11.7), Some(1.24), None)),
        0.77
      )))

    }
  }
}
