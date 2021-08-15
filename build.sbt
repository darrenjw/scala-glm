// sbt build file for scala-glm

name := "scala-glm"
organization := "com.github.darrenjw"
version := "0.6"

scalacOptions ++= Seq(
  "-unchecked", "-deprecation", "-feature"
)

libraryDependencies  ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.9" % "test",
  "org.scalactic" %% "scalactic" % "3.2.9" % "test",
  "org.scalatestplus" %% "scalacheck-1-15" % "3.2.9.0" % "test",
  "org.scalanlp" %% "breeze" % "2.0-RC1",
  "org.scalanlp" %% "breeze-viz" % "2.0-RC1",
  "org.scalanlp" %% "breeze-natives" % "2.0-RC1",
  ("org.ddahl" %% "rscala" % "3.2.19").cross(CrossVersion.for3Use2_13)
)

mdocIn := file("mdoc/")

mdocOut := file("docs/")

enablePlugins(MdocPlugin)

resolvers ++= Seq(
  "Sonatype Snapshots" at
    "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype Releases" at
    "https://oss.sonatype.org/content/repositories/releases/"
)

scalaVersion := "3.0.1"

crossScalaVersions := Seq("2.12.12", "2.13.5", "3.0.1")

ThisBuild / versionScheme := Some("strict")

fork := true

// eof


