/* Copyright (c) 2015 Andrée Ekroth.
 * Distributed under the MIT License (MIT).
 * See accompanying file LICENSE or copy at
 * http://opensource.org/licenses/MIT
 */

package com.github.ekroth
package concert
package api

object SongkickOps {
  case class ArtistSearch(artist: String) extends Op[Seq[songkick.Artist]]
  case class ArtistEvents(id: Int) extends Op[Seq[songkick.Event]]
}

trait SongkickSyntax {
  import scalaz._
  import Scalaz._

  import SongkickOps._

  def artistSearch(artist: String) = Free.liftFC(SongkickOps.ArtistSearch(artist))
  def artistEvents(id: Int) = Free.liftFC(SongkickOps.ArtistEvents(id))
}

trait Songkick {
  val SongkickAPI: SongkickSyntax = new SongkickSyntax {}
}
