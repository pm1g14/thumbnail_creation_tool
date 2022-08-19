import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val mainDependencies = Seq()
val testDependencies = Seq()

lazy val root = (project in file("."))
  .settings(
    name := "ct-coding-exercise",
    libraryDependencies ++= mainDependencies ++ testDependencies
  )
