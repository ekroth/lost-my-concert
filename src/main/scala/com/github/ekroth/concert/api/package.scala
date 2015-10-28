/* Copyright (c) 2015 Andrée Ekroth.
 * Distributed under the MIT License (MIT).
 * See accompanying file LICENSE or copy at
 * http://opensource.org/licenses/MIT
 */

package com.github.ekroth
package concert

package object api {
  import scalaz._
  import Scalaz._

  trait Op[A]
  type Prog[A] = Free.FreeC[Op, A]
  implicit val monadOp: Monad[Prog] = Free.freeMonad[({type λ[α] = Coyoneda[Op, α]})#λ]
}
