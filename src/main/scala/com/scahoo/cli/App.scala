package com.scahoo
package cli

object App extends App with FreeScahooInstructions {
  val whatever = for {
    contents <- readFile("path")
    references <- parseFile(contents)
    result <- search(contents)
  } yield references ::: result

  println(whatever.foldMap(ScahooLang.EvalEffectToID))
}
