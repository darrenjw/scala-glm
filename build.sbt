name := "scala-glm"
organization := "com.github.darrenjw"
version := "0.3-SNAPSHOT"

scalacOptions ++= Seq(
  "-unchecked", "-deprecation", "-feature"
)

libraryDependencies  ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalanlp" %% "breeze" % "0.13",
  "org.scalanlp" %% "breeze-viz" % "0.13",
  "org.scalanlp" %% "breeze-natives" % "0.13",
  "org.ddahl" %% "rscala" % "2.0.1"
)

resolvers ++= Seq(
  "Sonatype Snapshots" at
    "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype Releases" at
    "https://oss.sonatype.org/content/repositories/releases/"
)

// scalaVersion := "2.11.11"
scalaVersion := "2.12.1"

crossScalaVersions := Seq("2.11.11","2.12.1")

publishTo := Some(Resolver.sftp("Personal mvn repo", "unix.ncl.ac.uk", "/home/ucs/100/ndjw1/public_html/mvn"))

// sonatype stuff...

//publishTo := {
//  val nexus = "https://oss.sonatype.org/"
//  if (isSnapshot.value)
//    Some("snapshots" at nexus + "content/repositories/snapshots")
//  else
//    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
//}

pomIncludeRepository := { _ => false }

licenses := Seq("Apache2" -> url("https://opensource.org/licenses/Apache-2.0"))

homepage := Some(url("https://github.com/darrenjw/scala-glm/blob/master/README.md"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/darrenjw/scala-glm"),
    "scm:git@github.com:darrenjw/scala-glm.git"
  )
)

developers := List(
  Developer(
    id    = "darrenjw",
    name  = "Darren J Wilkinson",
    email = "darrenjwilkinson@btinternet.com",
    url   = url("https://github.com/darrenjw")
  )
)

publishMavenStyle := true

publishArtifact in Test := false


// eof


