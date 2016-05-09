package com.scahoo
package cli

object App extends App {
  import ScahooMonad._,
         scalaz._,
         effect.LiftIO,
         LiftIO._,
         Scalaz._

  def interp[P[_], A](implicit
    S: ScahooLanguage[P],
    M: Monad[P]
  ) = for {
    contents <- S.readFile("src/main/scala/com/scahoo/cli/Effects.scala")
  } yield contents

  val result = interp.foldMap(ScahooEffectToID)

  println(result)
}
