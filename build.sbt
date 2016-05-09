organization := "com.scahoo"

name := "cli"

version := "0.1-SNAPSHOT"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.7.1")

val scalazVersion = "7.2.0"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % scalazVersion,
  "org.scalaz" %% "scalaz-effect" % scalazVersion,
  "org.scalaz" %% "scalaz-concurrent" % scalazVersion,
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)

javacOptions in ThisBuild ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

scalacOptions ++= Seq(
  "-unchecked", 
  "-deprecation", 
  "-feature", 
  "-language:higherKinds")

initialCommands in console := """
  | import org.scalacheck._, Gen._, Arbitrary._, Prop._
  | import org.hablapps.fpinscalaz._
  | import fun._, fun.exercises._
  | import monads._, monads.exercises._
  | import freemonads._, freemonads.exercises._
  | import coproducts._, coproducts.exercises._
  |""".stripMargin
