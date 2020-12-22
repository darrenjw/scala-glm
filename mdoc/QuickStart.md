# scala-glm - Quickstart Guide

## PCA

This library contains code for principal components analysis based on a thin SVD of the centred data matrix. This is more numerically stable than a construction from the spectral decomposition of the covariance matrix. It is analogous to the R function `prcomp` rather than the R function `princomp`.
```scala mdoc
import scalaglm.Pca
import breeze.linalg._
val X = DenseMatrix((1.0,1.5),(1.5,2.0),(2.0,1.5))
val pca = Pca(X, List("V1","V2"))
pca.sdev
pca.loadings
pca.scores
pca.plots
pca.summary
```
The final line prints a readable summary of the PCA to the console. `plots` produces some diagnostic plots, including a "scree plot".

Note that there is also a utility function `pairs` for producing a "scatterplot matrix":
```scala mdoc
import scalaglm.Utils.pairs
pairs(X, List("V1", "V2"))
```

## Linear regression

This code computes regression coefficients and associated diagnostics via the QR decomposition of the covariate matrix. The diagnostics are analogous to those produced by the R function `lm`.

```scala mdoc:reset
import scalaglm.Lm
import breeze.linalg._
val y = DenseVector(1.0,2.0,1.0,1.5)
val X = DenseMatrix((1.0,1.5),(1.5,2.0),(2.0,1.5),(2.0,1.0))
val lm = Lm(y,X,List("V1","V2"))
lm.coefficients
lm.se
lm.fitted
lm.residuals
lm.studentised
val pred = lm.predict()
pred.fitted
pred.se
val predNew = lm.predict(DenseMatrix((1.1,1.6),(1.4,2.2),(1.6,2.1)))
predNew.fitted
predNew.se
lm.plots
lm.summary
```
The plots include a plot of studentised residuals against fitted values and a normal Q-Q plot for the studentised residuals.

## Generalised linear models

The current implementation supports only simple one-parameter exponential family observation models. This includes the most commonly used cases of **logistic regression** (`LogisticGlm`) and **Poisson regression** (`PoissonGlm`).

### Logistic regression

```scala mdoc:reset
import scalaglm.{Glm, LogisticGlm}
import breeze.linalg._
val y = DenseVector(1.0,0.0,1.0,0.0)
val X = DenseMatrix((1.0,1.5),(1.5,2.0),(2.0,1.5),(2.0,1.0))
val glm = Glm(y,X,List("V1","V2"),LogisticGlm)
glm.coefficients
glm.fitted
glm.predict(response=true).fitted
glm.summary
glm.plots
```

### Poisson regression

```scala mdoc:reset
import scalaglm.{Glm, PoissonGlm}
import breeze.linalg._
val y = DenseVector(1.0,3.0,4.0,2.0)
val X = DenseMatrix((1.0,1.5),(1.5,2.0),(2.0,1.5),(2.0,1.0))
val glm = Glm(y,X,List("V1","V2"),PoissonGlm)
glm.coefficients
glm.summary
glm.plots
```


