# scala-glm - Quickstart Guide

## PCA

This library contains code for principal components analysis based on a thin SVD of the centred data matrix. This is more numerically stable than a construction from the spectral decomposition of the covariance matrix. It is analogous to the R function `prcomp` rather than the R function `princomp`.
```scala
import scalaglm.Pca
// import scalaglm.Pca
import breeze.linalg._
// import breeze.linalg._
val X = DenseMatrix((1.0,1.5),(1.5,2.0),(2.0,1.5))
// X: breeze.linalg.DenseMatrix[Double] =
// 1.0  1.5
// 1.5  2.0
// 2.0  1.5
val pca = Pca(X, List("V1","V2"))
// pca: scalaglm.Pca =
// Pca(1.0  1.5
// 1.5  2.0
// 2.0  1.5  ,List(V1, V2))
pca.sdev
// res0: breeze.linalg.DenseVector[Double] = DenseVector(0.5, 0.28867513459481287)
pca.loadings
// res1: breeze.linalg.DenseMatrix[Double] =
// 1.0  -0.0
// 0.0  -1.0
pca.scores
// res2: breeze.linalg.DenseMatrix[Double] =
// -0.5  0.16666666666666674
// 0.0   -0.33333333333333326
// 0.5   0.16666666666666674
pca.plots
// res3: breeze.plot.Figure = breeze.plot.Figure@712a8ce7
pca.summary
// Standard deviations:
// V1	V2
//  0.500	 0.289
// Cumulative proportion of variance explained:
// V1	V2
//  0.750	 1.000
// Loadings:
// PC01	PC02	
//  1.000	-0.000	V1
//  0.000	-1.000	V2
```
The final line prints a readable summary of the PCA to the console. `plots` produces some diagnostic plots, including a "scree plot".

Note that there is also a utility function `pairs` for producing a "scatterplot matrix":
```scala
import scalaglm.Utils.pairs
// import scalaglm.Utils.pairs
pairs(X, List("V1", "V2"))
// res5: breeze.plot.Figure = breeze.plot.Figure@13c24886
```

## Linear regression

This code computes regression coefficients and associated diagnostics via the QR decomposition of the covariate matrix. The diagnostics are analogous to those produced by the R function `lm`.

```scala
import scalaglm.Lm
// import scalaglm.Lm
import breeze.linalg._
// import breeze.linalg._
val y = DenseVector(1.0,2.0,1.0,1.5)
// y: breeze.linalg.DenseVector[Double] = DenseVector(1.0, 2.0, 1.0, 1.5)
val X = DenseMatrix((1.0,1.5),(1.5,2.0),(2.0,1.5),(2.0,1.0))
// X: breeze.linalg.DenseMatrix[Double] =
// 1.0  1.5
// 1.5  2.0
// 2.0  1.5
// 2.0  1.0
val lm = Lm(y,X,List("V1","V2"))
// lm: scalaglm.Lm =
// Lm(DenseVector(1.0, 2.0, 1.0, 1.5),1.0  1.5
// 1.5  2.0
// 2.0  1.5
// 2.0  1.0  ,List(V1, V2),true)
lm.coefficients
// res6: breeze.linalg.DenseVector[Double] = DenseVector(-0.16666666666666852, 0.3333333333333338, 0.6666666666666674)
lm.se
// res7: breeze.linalg.DenseVector[Double] = DenseVector(2.718251071716681, 0.942809041582063, 1.1055415967851332)
lm.fitted
// res8: breeze.linalg.DenseVector[Double] = DenseVector(1.1666666666666663, 1.666666666666667, 1.5000000000000002, 1.1666666666666665)
lm.residuals
// res9: breeze.linalg.DenseVector[Double] = DenseVector(-0.1666666666666663, 0.33333333333333304, -0.5000000000000002, 0.3333333333333335)
lm.studentised
// res10: breeze.linalg.DenseVector[Double] = DenseVector(-0.9999999999999954, 0.9999999999999996, -1.0000000000000002, 1.0000000000000002)
val pred = lm.predict()
// pred: scalaglm.PredictLm =
// PredictLm(Lm(DenseVector(1.0, 2.0, 1.0, 1.5),1.0  1.5
// 1.5  2.0
// 2.0  1.5
// 2.0  1.0  ,List(V1, V2),true),1.0  1.5
// 1.5  2.0
// 2.0  1.5
// 2.0  1.0  )
pred.fitted
// res11: breeze.linalg.DenseVector[Double] = DenseVector(1.1666666666666665, 1.666666666666667, 1.5000000000000002, 1.1666666666666665)
pred.se
// res12: breeze.linalg.DenseVector[Double] = DenseVector(0.6871842709362765, 0.6236095644623235, 0.5, 0.6236095644623237)
val predNew = lm.predict(DenseMatrix((1.1,1.6),(1.4,2.2),(1.6,2.1)))
// predNew: scalaglm.PredictLm =
// PredictLm(Lm(DenseVector(1.0, 2.0, 1.0, 1.5),1.0  1.5
// 1.5  2.0
// 2.0  1.5
// 2.0  1.0  ,List(V1, V2),true),1.1  1.6
// 1.4  2.2
// 1.6  2.1  )
predNew.fitted
// res13: breeze.linalg.DenseVector[Double] = DenseVector(1.2666666666666666, 1.766666666666667, 1.7666666666666673)
predNew.se
// res14: breeze.linalg.DenseVector[Double] = DenseVector(0.5792715732327587, 0.7930251502246882, 0.7431165603202652)
lm.plots
// res15: breeze.plot.Figure = breeze.plot.Figure@4df3385
lm.summary
// Estimate	 S.E.	 t-stat	p-value		Variable
// ---------------------------------------------------------
//  -0.1667	 2.718	-0.061	0.9610  	(Intercept)
//   0.3333	 0.943	 0.354	0.7837  	V1
//   0.6667	 1.106	 0.603	0.6545  	V2
// Residual standard error:   0.7071 on 1 degrees of freedom
// Multiple R-squared: 0.2727, Adjusted R-squared: -1.1818
// F-statistic: 0.1875 on 2 and 1 DF, p-value: 0.85280
```
The plots include a plot of studentised residuals against fitted values and a normal Q-Q plot for the studentised residuals.

## Generalised linear models

The current implementation supports only simple one-parameter exponential family observation models. This includes the most commonly used cases of **logistic regression** (`LogisticGlm`) and **Poisson regression** (`PoissonGlm`).

### Logistic regression

```scala
import scalaglm.{Glm, LogisticGlm}
// import scalaglm.{Glm, LogisticGlm}
import breeze.linalg._
// import breeze.linalg._
val y = DenseVector(1.0,0.0,1.0,0.0)
// y: breeze.linalg.DenseVector[Double] = DenseVector(1.0, 0.0, 1.0, 0.0)
val X = DenseMatrix((1.0,1.5),(1.5,2.0),(2.0,1.5),(2.0,1.0))
// X: breeze.linalg.DenseMatrix[Double] =
// 1.0  1.5
// 1.5  2.0
// 2.0  1.5
// 2.0  1.0
val glm = Glm(y,X,List("V1","V2"),LogisticGlm)
// glm: scalaglm.Glm =
// Glm(DenseVector(1.0, 0.0, 1.0, 0.0),1.0  1.5
// 1.5  2.0
// 2.0  1.5
// 2.0  1.0  ,List(V1, V2),LogisticGlm,true,50)
glm.coefficients
// res17: breeze.linalg.DenseVector[Double] = DenseVector(4.664616774081789, -1.952393755116432, -0.9761968775582166)
glm.fitted
// res18: breeze.linalg.DenseVector[Double] = DenseVector(0.7769409304492391, 0.44611813910152176, 0.3308227913477173, 0.44611813910152176)
glm.predict().fitted
// res19: breeze.linalg.DenseVector[Double] = DenseVector(1.2479277026280322, -0.2163676137092918, -0.7044660524883997, -0.21636761370929136)
glm.predict(response=true).fitted
// res20: breeze.linalg.DenseVector[Double] = DenseVector(0.7769409304492391, 0.44611813910152176, 0.3308227913477173, 0.44611813910152176)
glm.summary
// Estimate	 S.E.	 z-stat	p-value		Variable
// ---------------------------------------------------------
//   4.6646	 8.655	 0.539	0.5899  	(Intercept)
//  -1.9524	 3.085	-0.633	0.5269  	V1
//  -0.9762	 3.236	-0.302	0.7629  	V2
glm.plots
// res22: breeze.plot.Figure = breeze.plot.Figure@fc996bf
```

### Poisson regression

```scala
import scalaglm.{Glm, PoissonGlm}
// import scalaglm.{Glm, PoissonGlm}
import breeze.linalg._
// import breeze.linalg._
val y = DenseVector(1.0,3.0,4.0,2.0)
// y: breeze.linalg.DenseVector[Double] = DenseVector(1.0, 3.0, 4.0, 2.0)
val X = DenseMatrix((1.0,1.5),(1.5,2.0),(2.0,1.5),(2.0,1.0))
// X: breeze.linalg.DenseMatrix[Double] =
// 1.0  1.5
// 1.5  2.0
// 2.0  1.5
// 2.0  1.0
val glm = Glm(y,X,List("V1","V2"),PoissonGlm)
// glm: scalaglm.Glm =
// Glm(DenseVector(1.0, 3.0, 4.0, 2.0),1.0  1.5
// 1.5  2.0
// 2.0  1.5
// 2.0  1.0  ,List(V1, V2),PoissonGlm,true,50)
glm.coefficients
// res23: breeze.linalg.DenseVector[Double] = DenseVector(-3.1262819779336546, 1.4099402205461313, 1.0832924624611122)
glm.summary
// Estimate	 S.E.	 z-stat	p-value		Variable
// ---------------------------------------------------------
//  -3.1263	 3.262	-0.958	0.3379  	(Intercept)
//   1.4099	 1.162	 1.213	0.2250  	V1
//   1.0833	 1.055	 1.027	0.3044  	V2
glm.plots
// res25: breeze.plot.Figure = breeze.plot.Figure@62e47649
```


