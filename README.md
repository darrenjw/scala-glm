# scala-glm

### Scala library for fitting linear and generalised linear statistical models

## Installation/Getting started

This library is simplest to use with SBT.

If building from source, running `sbt console` should give a Scala REPL with a dependence on the library. Documentation can be generated with `sbt doc`.

To use the pre-built binary, add the following lines to your `build.sbt`:
```scala
libraryDependencies += "darrenjw" %% "scala-glm" % "0.1"
resolvers += "Newcastle mvn repo" at "https://www.staff.ncl.ac.uk/d.j.wilkinson/mvn/"
```
It's currently only published to my own personal repo. I'll figure out how to push it to Sonatype once it's properly tested.

If you just want to try it out without setting up a project, you can do so with a session like:
```
$ sbt
> set scalaVersion := "2.12.1"
> set libraryDependencies += "darrenjw" %% "scala-glm" % "0.1"
> set resolvers += "Newcastle mvn repo" at "https://www.staff.ncl.ac.uk/d.j.wilkinson/mvn/"
> console
scala> import scalaglm._
```
See below for usage info.

This library has a dependence on Breeze, so if you have a dependence on `scala-glm` you don't need to add an additional dependence on Breeze.


## PCA

This library contains code for principal components analysis based on a thin SVD of the centred data matrix. This is more numerically stable than a construction from the spectral decomposition of the covariance matrix. It is analogous to the R function `prcomp` rather than the R function `princomp`.

```scala
import scalaglm.Pca
import breeze.linalg._
val X = DenseMatrix((1.0,1.5),(1.5,2.0),(2.0,1.5))
val pca = Pca(X, List("V1","V2"))
pca.sdev
pca.loadings
pca.scores
pca.summary
```
The final line prints a readable summary of the PCA to the console.

## Linear regression

This code computes regression coefficients and associated diagnostics via the QR decomposition of the covariate matrix. The diagnostics are analogous to those produced by the R function `lm`.

```scala
import scalaglm.Lm
import breeze.linalg._
val y = DenseVector(1.0,2.0,1.0,1.5)
val X = DenseMatrix((1.0,1.5),(1.5,2.0),(2.0,1.5),(2.0,1.0))
val lm = Lm(y,X,List("V1","V2"))
lm.coefficients
lm.summary
```
The final line produces the following output to the console:
```
scala> lm.summary
Estimate	 S.E.	 t-stat	p-value		Variable
---------------------------------------------------------
 -0.1667	 2.718	-0.061	0.9610  	(Intercept)
  0.3333	 0.943	 0.354	0.7837  	V1
  0.6667	 1.106	 0.603	0.6545  	V2

Residual standard error:   0.7071 on 1 degrees of freedom
Multiple R-squared: 0.2727, Adjusted R-squared: -1.1818
F-statistic: 0.1875 on 2 and 1 DF, p-value: 0.85280
```

## Generalised linear models

The current implementation supports only simple one-parameter exponential family observation models. This includes the most commonly used cases of **logistic regression** (`LogisticGlm`) and **Poisson regression** (`PoissonGlm`).

### Logistic regression

```scala
import scalaglm.{Glm, LogisticGlm}
import breeze.linalg._
val y = DenseVector(1.0,0.0,1.0,0.0)
val X = DenseMatrix((1.0,1.5),(1.5,2.0),(2.0,1.5),(2.0,1.0))
val glm = Glm(y,X,List("V1","V2"),LogisticGlm)
glm.coefficients
glm.summary
```

### Poisson regression

```scala
import scalaglm.{Glm, PoissonGlm}
import breeze.linalg._
val y = DenseVector(1.0,3.0,4.0,2.0)
val X = DenseMatrix((1.0,1.5),(1.5,2.0),(2.0,1.5),(2.0,1.0))
val glm = Glm(y,X,List("V1","V2"),PoissonGlm)
glm.coefficients
glm.summary
```



#### eof

