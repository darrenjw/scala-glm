// sbt build file for scala-glm

name := "scala-glm"
organization := "com.github.darrenjw"
version := "0.9"

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.19" % "test",
  "org.scalactic" %% "scalactic" % "3.2.19" % "test",
  "org.scalatestplus" %% "scalacheck-1-18" % "3.2.19.0" % "test",
  "org.scalanlp" %% "breeze" % "2.1.0",
  "org.scalanlp" %% "breeze-viz" % "2.1.0",
  // "org.scalanlp" %% "breeze-natives" % "2.1.0",
  // "dev.ludovic.netlib" % "blas" % "3.0.3" withSources(),
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

scalaVersion := "3.3.4"

crossScalaVersions := Seq("2.12.19", "2.13.15", "3.3.4")

ThisBuild / versionScheme := Some("strict")

fork := true

// eof
