import sbt.Keys._
import sbt._

object Build extends Build {
  lazy val root =
    Project(id = "rabb-it", base = file("."))
      .configs(IntegrationTest)
      .settings(Defaults.itSettings: _*)
      .settings(
        name := "rabb-it",
        organization := "uk.gov.homeoffice",
        version := "1.0-SNAPSHOT",
        scalaVersion := "2.11.6",
        scalacOptions ++= Seq(
          "-feature",
          "-language:implicitConversions",
          "-language:higherKinds",
          "-language:existentials",
          "-language:reflectiveCalls",
          "-language:postfixOps",
          "-Yrangepos"),
        resolvers ++= Seq(
          "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
          "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
          "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
          "Kamon Repository" at "http://repo.kamon.io"),
        libraryDependencies ++= Seq(
          "com.typesafe" % "config" % "1.2.1" withSources(),
          "com.typesafe.akka" %% "akka-actor" % "2.3.9" withSources(),
          "com.typesafe.akka" %% "akka-stream-experimental" % "1.0-M5" withSources(),
          "com.rabbitmq" % "amqp-client" % "3.5.0" withSources(),
          "org.json4s" %% "json4s-native" % "3.2.11" withSources(),
          "org.json4s" %% "json4s-ext" % "3.2.11" withSources(),
          "org.scalautils" %% "scalautils" % "2.1.5" withSources(),
          "uk.gov.homeoffice" %% "io-it" % "1.0-SNAPSHOT" withSources()),
        libraryDependencies ++= Seq(
          "org.specs2" %% "specs2" % "2.4.17" % "test, it" withSources()))
}