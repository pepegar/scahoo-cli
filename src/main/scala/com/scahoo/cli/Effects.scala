package com.scahoo
package cli

import scalaz.Free,
       scalaz.~>,
       scalaz.Id._,
       scalaz.Monad

case class Reference(signature: String, name: String)
sealed trait ScahooEffect[A]
case class ReadFile(path: String) extends ScahooEffect[String]
case class ParseFile(contents: String) extends ScahooEffect[List[Reference]]
case class Search(tpe: String) extends ScahooEffect[List[Reference]]

abstract trait ScahooLanguage[P[_]] {
  def readFile(path: String): P[String]
  def parseFile(contents: String): P[List[Reference]]
  def search(tpe: String): P[List[Reference]]
}

object ScahooMonad {
  object ScahooEffectToID extends (ScahooEffect ~> Id) {
    def apply[A](eff: ScahooEffect[A]): A = eff match {
      case ReadFile(path) => io.Source.fromFile(path).mkString("")
      case ParseFile(contents) => Nil
      case Search(tpe) => Nil
    }
  }

  type FreeScahoo[P] = Free[ScahooEffect, P]

  trait ScahooMonad extends Monad[FreeScahoo] {
    def point[A](a: => A): FreeScahoo[A] = Free.point(a)
    def bind[A, B](fa: FreeScahoo[A])(f: A => FreeScahoo[B]): FreeScahoo[B] = fa flatMap f
  }

  implicit def freeScahooInstructions = new ScahooLanguage[FreeScahoo] with ScahooMonad {
    def readFile(path: String): Free[ScahooEffect, String] = Free.liftF(ReadFile(path))
    def parseFile(contents: String): Free[ScahooEffect, List[Reference]] = Free.liftF(ParseFile(contents))
    def search(tpe: String): Free[ScahooEffect, List[Reference]] = Free.liftF(Search(tpe))
  }
}
