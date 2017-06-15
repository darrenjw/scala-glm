# scala-glm

### Scala library for regression modelling (fitting linear and generalised linear statistical models, diagnosing fit, making predictions)

## Installation/Getting started

This library is simplest to use with [SBT](http://www.scala-sbt.org/). You should install SBT before attempting to use this library.

To use the pre-built binary, add the following lines to your `build.sbt`:
```scala
libraryDependencies += "darrenjw" %% "scala-glm" % "0.2"
resolvers += "Newcastle mvn repo" at "https://www.staff.ncl.ac.uk/d.j.wilkinson/mvn/"
```
The current stable release is "0.2". The latest unstable release is "0.3-SNAPSHOT".

It's currently only published to my own personal repo. I'll figure out how to push it to Sonatype soon.

If you just want to try it out without setting up a project, you can do so with a session like:
```
$ sbt
> set scalaVersion := "2.12.1"
> set libraryDependencies += "darrenjw" %% "scala-glm" % "0.2"
> set resolvers += "Newcastle mvn repo" at "https://www.staff.ncl.ac.uk/d.j.wilkinson/mvn/"
> console
scala> import scalaglm._
```
See below for usage info.

This library has a dependence on Breeze, so if you have a dependence on `scala-glm` you don't need to add an additional dependence on Breeze.

### Building from source

If building from source, running `sbt console` from this directory should give a Scala REPL with a dependence on the library. Running `sbt test` will run all tests (but note that an [**R**](https://www.r-project.org/) installation is required for many of the tests). Running `sbt doc` will generate ScalaDoc API documentation.

## Documentation

* [QuickStart Guide](https://darrenjw.github.io/scala-glm/QuickStart.html) - start with this to get a feeling for what the library can do
* The [examples subdirectory](examples/src/main/scala/) of this repo contains more interesting, self-contained runnable examples
* [API documentation](https://darrenjw.github.io/scala-glm/api/scalaglm/) - ScalaDoc (for the most recent snapshot)



