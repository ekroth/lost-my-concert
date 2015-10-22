/* Copyright (c) 2015 Andrée Ekroth.
 * Distributed under the MIT License (MIT).
 * See accompanying file LICENSE or copy at
 * http://opensource.org/licenses/MIT
 */

package com.github.ekroth
package concert
package controllers

import scala.concurrent.ExecutionContext

object Contexts {
  implicit val execContext: ExecutionContext = ExecutionContext.Implicits.global
}
