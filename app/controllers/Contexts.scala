package com.github.ekroth
package controllers

import scala.concurrent.ExecutionContext

object Contexts {
  implicit val execContext: ExecutionContext = ExecutionContext.Implicits.global
}
