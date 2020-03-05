name := """Location-Api"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.12"

libraryDependencies += filters
libraryDependencies += ws

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.1" % Test
libraryDependencies += "org.mockito" % "mockito-all" % "1.10.19" % Test
libraryDependencies += "com.github.tomakehurst"   % "wiremock" % "2.26.1" % Test
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
