# scala-glm - Quickstart Guide

## PCA

This library contains code for principal components analysis based on a thin SVD of the centred data matrix. This is more numerically stable than a construction from the spectral decomposition of the covariance matrix. It is analogous to the R function `prcomp` rather than the R function `princomp`.
```scala
import scalaglm.Pca
import breeze.linalg._
val X = DenseMatrix((1.0,1.5),(1.5,2.0),(2.0,1.5))
// X: DenseMatrix[Double] = 1.0  1.5  
// 1.5  2.0  
// 2.0  1.5  
val pca = Pca(X, List("V1","V2"))
// pca: Pca = Pca(
//   mat = 1.0  1.5  
// 1.5  2.0  
// 2.0  1.5  ,
//   colNames = List("V1", "V2")
// )
pca.sdev
// res0: DenseVector[Double] = DenseVector(0.5, 0.28867513459481287)
pca.loadings
// res1: DenseMatrix[Double] = 1.0  -0.0  
// 0.0  -1.0  
pca.scores
// res2: DenseMatrix[Double] = -0.5  0.16666666666666674   
// 0.0   -0.33333333333333326  
// 0.5   0.16666666666666674   
pca.plots
// res3: breeze.plot.Figure = breeze.plot.Figure@713e35a1
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
pairs(X, List("V1", "V2"))
// res5: breeze.plot.Figure = breeze.plot.Figure@2eb9f673
```

## Linear regression

This code computes regression coefficients and associated diagnostics via the QR decomposition of the covariate matrix. The diagnostics are analogous to those produced by the R function `lm`.

```scala
import scalaglm.Lm
import breeze.linalg._
val y = DenseVector(1.0,2.0,1.0,1.5)
// y: DenseVector[Double] = DenseVector(1.0, 2.0, 1.0, 1.5)
val X = DenseMatrix((1.0,1.5),(1.5,2.0),(2.0,1.5),(2.0,1.0))
// X: DenseMatrix[Double] = 1.0  1.5  
// 1.5  2.0  
// 2.0  1.5  
// 2.0  1.0  
val lm = Lm(y,X,List("V1","V2"))
// lm: Lm = Lm(
//   y = DenseVector(1.0, 2.0, 1.0, 1.5),
//   Xmat = 1.0  1.5  
// 1.5  2.0  
// 2.0  1.5  
// 2.0  1.0  ,
//   colNames = List("V1", "V2"),
//   addIntercept = true
// )
lm.coefficients
// res7: DenseVector[Double] = DenseVector(-0.16666666666666619, 0.33333333333333337, 0.6666666666666663)
lm.se
// res8: DenseVector[Double] = DenseVector(2.718251071716681, 0.942809041582063, 1.1055415967851332)
lm.fitted
// res9: DenseVector[Double] = DenseVector(1.1666666666666665, 1.6666666666666665, 1.5, 1.1666666666666667)
lm.residuals
// res10: DenseVector[Double] = DenseVector(-0.16666666666666652, 0.3333333333333335, -0.5, 0.33333333333333326)
lm.studentised
// res11: DenseVector[Double] = DenseVector(-0.9999999999999969, 1.0000000000000009, -0.9999999999999999, 1.0000000000000002)
val pred = lm.predict()
// pred: scalaglm.PredictLm = PredictLm(
//   mod = Lm(
//     y = DenseVector(1.0, 2.0, 1.0, 1.5),
//     Xmat = 1.0  1.5  
// 1.5  2.0  
// 2.0  1.5  
// 2.0  1.0  ,
//     colNames = List("V1", "V2"),
//     addIntercept = true
//   ),
//   newX = 1.0  1.5  
// 1.5  2.0  
// 2.0  1.5  
// 2.0  1.0  
// )
pred.fitted
// res12: DenseVector[Double] = DenseVector(1.1666666666666665, 1.6666666666666665, 1.5, 1.166666666666667)
pred.se
// res13: DenseVector[Double] = DenseVector(0.6871842709362767, 0.6236095644623235, 0.4999999999999999, 0.6236095644623237)
val predNew = lm.predict(DenseMatrix((1.1,1.6),(1.4,2.2),(1.6,2.1)))
// predNew: scalaglm.PredictLm = PredictLm(
//   mod = Lm(
//     y = DenseVector(1.0, 2.0, 1.0, 1.5),
//     Xmat = 1.0  1.5  
// 1.5  2.0  
// 2.0  1.5  
// 2.0  1.0  ,
//     colNames = List("V1", "V2"),
//     addIntercept = true
//   ),
//   newX = 1.1  1.6  
// 1.4  2.2  
// 1.6  2.1  
// )
predNew.fitted
// res14: DenseVector[Double] = DenseVector(1.2666666666666668, 1.7666666666666664, 1.7666666666666666)
predNew.se
// res15: DenseVector[Double] = DenseVector(0.5792715732327586, 0.793025150224688, 0.7431165603202654)
lm.plots
// res16: breeze.plot.Figure = breeze.plot.Figure@6eb58cd5
lm.summary
// Estimate	 S.E.	 t-stat	p-value		Variable
// ---------------------------------------------------------
//  -0.1667	 2.718	-0.061	0.9610  	(Intercept)
//   0.3333	 0.943	 0.354	0.7837  	V1
//   0.6667	 1.106	 0.603	0.6545  	V2
// 
// Residual standard error:   0.7071 on 1 degrees of freedom
// Multiple R-squared: 0.2727, Adjusted R-squared: -1.1818
// F-statistic: 0.1875 on 2 and 1 DF, p-value: 0.85280
//
```
The plots include a plot of studentised residuals against fitted values and a normal Q-Q plot for the studentised residuals.

## Generalised linear models

The current implementation supports only simple one-parameter exponential family observation models. This includes the most commonly used cases of **logistic regression** (`LogisticGlm`) and **Poisson regression** (`PoissonGlm`).

### Logistic regression

```scala
import scalaglm.{Glm, LogisticGlm}
import breeze.linalg._
val y = DenseVector(1.0,0.0,1.0,0.0)
// y: DenseVector[Double] = DenseVector(1.0, 0.0, 1.0, 0.0)
val X = DenseMatrix((1.0,1.5),(1.5,2.0),(2.0,1.5),(2.0,1.0))
// X: DenseMatrix[Double] = 1.0  1.5  
// 1.5  2.0  
// 2.0  1.5  
// 2.0  1.0  
val glm = Glm(y,X,List("V1","V2"),LogisticGlm)
// glm: Glm = Glm(
//   y = DenseVector(1.0, 0.0, 1.0, 0.0),
//   Xmat = 1.0  1.5  
// 1.5  2.0  
// 2.0  1.5  
// 2.0  1.0  ,
//   colNames = List("V1", "V2"),
//   fam = LogisticGlm,
//   addIntercept = true,
//   its = 50
// )
glm.coefficients
// res19: DenseVector[Double] = DenseVector(4.664616774081783, -1.9523937551164303, -0.976196877558214)
glm.fitted
// res20: DenseVector[Double] = DenseVector(0.776940930449239, 0.4461181391015221, 0.3308227913477176, 0.44611813910152176)
glm.predict(response=true).fitted
// res21: DenseVector[Double] = DenseVector(0.776940930449239, 0.4461181391015221, 0.3308227913477176, 0.44611813910152176)
glm.summary
// Estimate	 S.E.	 z-stat	p-value		Variable
// ---------------------------------------------------------
//   4.6646	 8.655	 0.539	0.5899  	(Intercept)
//  -1.9524	 3.085	-0.633	0.5269  	V1
//  -0.9762	 3.236	-0.302	0.7629  	V2
glm.plots
// res23: breeze.plot.Figure = breeze.plot.Figure@734c8738
```

### Poisson regression

```scala
import scalaglm.{Glm, PoissonGlm}
import breeze.linalg._
val y = DenseVector(1.0,3.0,4.0,2.0)
// y: DenseVector[Double] = DenseVector(1.0, 3.0, 4.0, 2.0)
val X = DenseMatrix((1.0,1.5),(1.5,2.0),(2.0,1.5),(2.0,1.0))
// X: DenseMatrix[Double] = 1.0  1.5  
// 1.5  2.0  
// 2.0  1.5  
// 2.0  1.0  
val glm = Glm(y,X,List("V1","V2"),PoissonGlm)
// glm: Glm = Glm(
//   y = DenseVector(1.0, 3.0, 4.0, 2.0),
//   Xmat = 1.0  1.5  
// 1.5  2.0  
// 2.0  1.5  
// 2.0  1.0  ,
//   colNames = List("V1", "V2"),
//   fam = PoissonGlm,
//   addIntercept = true,
//   its = 50
// )
glm.coefficients
// res25: DenseVector[Double] = DenseVector(-3.126281977933656, 1.4099402205461318, 1.0832924624611127)
glm.summary
// Estimate	 S.E.	 z-stat	p-value		Variable
// ---------------------------------------------------------
//  -3.1263	 3.262	-0.958	0.3379  	(Intercept)
//   1.4099	 1.162	 1.213	0.2250  	V1
//   1.0833	 1.055	 1.027	0.3044  	V2
glm.plots
// res27: breeze.plot.Figure = breeze.plot.Figure@5b8bda20
```


