package com.scahoo
package cli

import scalaz.Free, scalaz.~>, scalaz.Id._, scalaz.Monad

trait FreeScahooInstructions extends ScahooLanguage[Free[ScahooEffect, ?]]{
  import Free._

  def readFile(path: String): Free[ScahooEffect, String] = liftF(ReadFile(path))
  def parseFile(contents: String): Free[ScahooEffect, List[Reference]] = liftF(ParseFile(contents))
  def search(tpe: String): Free[ScahooEffect, List[Reference]] = liftF(Search(tpe))
}

case class Reference(signature: String, name: String)
sealed trait ScahooEffect[P]
case class ReadFile(path: String) extends ScahooEffect[String]
case class ParseFile(contents: String) extends ScahooEffect[List[Reference]]
case class Search(tpe: String) extends ScahooEffect[List[Reference]]

object ScahooLang {
  object EvalEffectToID extends (ScahooEffect ~> Id) {
    def apply[A](eff: ScahooEffect[A]): A = eff match {
      case ReadFile(path) => path
      case ParseFile(contents) => Nil
      case Search(tpe) => Nil
    }
  }
}

abstract trait ScahooLanguage[P[_]] {
  // Smart constructors
  def readFile(path: String): P[String]
  def parseFile(contents: String): P[List[Reference]]
  def search(tpe: String): P[List[Reference]]
}
