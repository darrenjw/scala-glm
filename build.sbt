// sbt build file for scala-glm

name := "scala-glm"
organization := "com.github.darrenjw"
version := "0.6-SNAPSHOT"

scalacOptions ++= Seq(
  "-unchecked", "-deprecation", "-feature"
)

libraryDependencies  ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.2" % "test",
  "org.scalactic" %% "scalactic" % "3.2.2" % "test",
  "org.scalatestplus" %% "scalacheck-1-14" % "3.2.2.0" % "test",
  "org.scalanlp" %% "breeze" % "1.1",
  "org.scalanlp" %% "breeze-viz" % "1.1",
  "org.scalanlp" %% "breeze-natives" % "1.1",
  "org.ddahl" %% "rscala" % "3.2.19"
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

// scalaVersion := "2.12.12"
scalaVersion := "2.13.5"

crossScalaVersions := Seq("2.12.12", "2.13.5")



// eof


