name := "scala-glm-examples"

version := "0.2"

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.14" % "test",
  "com.github.darrenjw" %% "scala-glm" % "0.9-SNAPSHOT",
  ("org.ddahl" %% "rscala" % "3.2.19").cross(CrossVersion.for3Use2_13)
)

resolvers ++= Seq(
  "Sonatype Snapshots" at
    "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype Releases" at
    "https://oss.sonatype.org/content/repositories/releases/"
)

scalaVersion := "3.3.0"
