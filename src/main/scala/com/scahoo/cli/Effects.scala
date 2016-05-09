package com.scahoo
package cli

import scalaz.Free,
       scalaz.~>,
       scalaz.Monad,
       scalaz.concurrent.Task

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
  // use Task for concurrency and error handling
  object ScahooEffectToFuture extends (ScahooEffect ~> Task) {
    def apply[A](eff: ScahooEffect[A]): Task[A] = eff match {
      case ReadFile(path) => Task {
        io.Source.fromFile(path).mkString("")
      }
      case ParseFile(contents) => Task { Nil }
      case Search(tpe) => Task { Nil }
    }
  }

  type FreeScahoo[P] = Free[ScahooEffect, P]

  trait ScahooMonad extends Monad[FreeScahoo] {
    def point[A](a: => A): FreeScahoo[A] = Free.point(a)
    def bind[A, B](fa: FreeScahoo[A])(f: A => FreeScahoo[B]): FreeScahoo[B] = fa flatMap f
  }

  trait FreeScahooInstructions extends ScahooLanguage[FreeScahoo] {
    def readFile(path: String): Free[ScahooEffect, String] = Free.liftF(ReadFile(path))
    def parseFile(contents: String): Free[ScahooEffect, List[Reference]] = Free.liftF(ParseFile(contents))
    def search(tpe: String): Free[ScahooEffect, List[Reference]] = Free.liftF(Search(tpe))
  }

  implicit val pimpedScahoo = new FreeScahooInstructions with ScahooMonad
}
