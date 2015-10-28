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

trait Logging extends Interpreter {

  import songkick._

  import api._
  import api.SongkickOps._

  import errorhandling._

  private val baseTrans = super.trans

  abstract override def trans = new (Op ~> ResultF) {
    override def apply[A](op: Op[A]): ResultF[A] = {
      println(s"exec: '$op'")
      baseTrans(op)
    }
  }
}
