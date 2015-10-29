/* Copyright (c) 2015 Andrée Ekroth.
 * Distributed under the MIT License (MIT).
 * See accompanying file LICENSE or copy at
 * http://opensource.org/licenses/MIT
 */

package com.github.ekroth
package concert

import scalaz._
import Scalaz._

trait Interpreter {
  def trans: api.Op ~> errorhandling.ResultF
}
