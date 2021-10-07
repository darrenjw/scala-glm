# scala-glm - Quickstart Guide

## PCA

This library contains code for principal components analysis based on a thin SVD of the centred data matrix. This is more numerically stable than a construction from the spectral decomposition of the covariance matrix. It is analogous to the R function `prcomp` rather than the R function `princomp`. First create some synthetic data.

```scala mdoc:silent
import breeze.linalg._
import breeze.numerics._
import breeze.stats.distributions._
import breeze.stats.distributions.Rand.VariableSeed.randBasis

val X = DenseMatrix.tabulate(100, 3)((i, j) => 
	Gaussian(j, j+1).sample())
```
Now we can do PCA.
```scala mdoc:silent
import scalaglm.Pca
val pca = Pca(X, List("V1", "V2", "V3"))
```
```scala mdoc
pca.sdev
pca.loadings
pca.scores
pca.plots
pca.summary
```
The final line prints a readable summary of the PCA to the console. `plots` produces some diagnostic plots, including a "scree plot".

```scala mdoc:invisible
val f1 = pca.plots
f1.saveas("docs/pca-plots.png")
```
![PCA Plots](pca-plots.png)

Note that there is also a utility function `pairs` for producing a "scatterplot matrix":
```scala mdoc
import scalaglm.Utils.pairs
pairs(X, List("V1", "V2", "V3"))
```

```scala mdoc:invisible
val f2 = pairs(X, List("V1", "V2", "V3"))
f2.saveas("docs/pairs-plots.png")
```
![Pairs plots](pairs-plots.png)


## Linear regression

This code computes regression coefficients and associated diagnostics via the QR decomposition of the covariate matrix. The diagnostics are analogous to those produced by the R function `lm`. We start by creating a synthetic response variable.

```scala mdoc:silent
val y = DenseVector.tabulate(100)(i => 
	Gaussian(2.0 + 1.5*X(i,0) + 0.5*X(i,1), 3.0).sample())
```
So we can now do linear regression and generate all of the usual diagnostics.
```scala mdoc:silent
import scalaglm.Lm
val lm = Lm(y,X,List("V1", "V2", "V3"))
```
```scala mdoc
lm.coefficients
lm.se
lm.fitted
lm.residuals
lm.studentised
val pred = lm.predict()
pred.fitted
pred.se
val predNew = lm.predict(DenseMatrix((1.1, 1.6, 1.0), (1.4, 2.2, 3.0)))
predNew.fitted
predNew.se
lm.plots
lm.summary
```
The plots include a plot of studentised residuals against fitted values and a normal Q-Q plot for the studentised residuals.

```scala mdoc:invisible
val f3 = lm.plots
f3.saveas("docs/lm-plots.png")
```
![Linear model plots](lm-plots.png)


## Generalised linear models

The current implementation supports only simple one-parameter exponential family observation models. This includes the most commonly used cases of **logistic regression** (`LogisticGlm`) and **Poisson regression** (`PoissonGlm`).

### Logistic regression

Again, we start by creating an appropriate response variable.
```scala mdoc:silent
val ylb = (0 until 100) map (i => Bernoulli(sigmoid(1.0 + X(i,0))).sample())
val yl = DenseVector(ylb.toArray map {b => if (b) 1.0 else 0.0})
```

Then we can do logistic regression in a typical way.
```scala mdoc:silent
import scalaglm.{Glm, LogisticGlm}
val glm = Glm(yl, X, List("V1","V2","V3"), LogisticGlm)
```
```scala mdoc
glm.coefficients
glm.fitted
glm.predict(response=true).fitted
glm.summary
glm.plots
```

```scala mdoc:invisible
val f4 = glm.plots
f4.saveas("docs/glm-plots.png")
```
![Logistic regression plots](glm-plots.png)


### Poisson regression

We first create an appropriate response, and then do Poisson regression.
```scala mdoc:silent
val yp = DenseVector.tabulate(100)(i => Poisson(math.exp(-0.5 + X(i,0))).sample().toDouble)

import scalaglm.PoissonGlm
val pglm = Glm(yp, X, List("V1","V2","V3"), PoissonGlm)
```
```scala mdoc
pglm.coefficients
pglm.summary
pglm.plots
```
```scala mdoc:invisible
val f5 = pglm.plots
f5.saveas("docs/pglm-plots.png")
```
![Poisson regression plots](pglm-plots.png)


## Non-linear response

The above covers the main functionality of the library based on a linear reponse to variation in covariate values. For flexible modelling of a nonlinear response, see the documentation on [flexible regression modelling](FlexibleRegression.md).

