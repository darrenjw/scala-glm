# scala-glm

### Scala library for fitting linear and generalised linear statistical models

## Installation/Getting started

This library is simplest to use with SBT.

If building from source, running `sbt console` should give a Scala REPL with a dependence on the library. Documentation can be generated with `sbt doc`.

This library has a dependence on Breeze, so if you have a dependence on `scala-glm` you don't need to add an additional dependence on Breeze.


## PCA

This library contains code for principal components analysis based on a thin SVD of the centred data matrix. This is more numerically stable than a construction from the spectral decomposition of the covariance matrix. It is analogous to the R function `prcomp` rather than the R function `princomp`.

```scala
import scalaglm.Pca
import breeze.linalg._
val X = DenseMatrix((1.0,1.5),(1.5,2.0),(2.0,1.5))
val pca = Pca(X)
pca.sdev
pca.loadings
pca.scores
```

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
```

### Poisson regression

```scala
import scalaglm.{Glm, PoissonGlm}
import breeze.linalg._
val y = DenseVector(1.0,3.0,4.0,2.0)
val X = DenseMatrix((1.0,1.5),(1.5,2.0),(2.0,1.5),(2.0,1.0))
val glm = Glm(y,X,List("V1","V2"),PoissonGlm)
glm.coefficients
```



#### eof

