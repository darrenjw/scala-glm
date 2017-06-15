// quickstart.scala
// script for the QuickStart guide

// PCA
import scalaglm.Pca
import breeze.linalg._
val X = DenseMatrix((1.0,1.5),(1.5,2.0),(2.0,1.5))
val pca = Pca(X, List("V1","V2"))
pca.sdev
pca.loadings
pca.scores
pca.plots
pca.summary

import scalaglm.Utils.pairs
pairs(X, List("V1", "V2"))


// Linear regression
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

// GLMs

// Logistic regression
import scalaglm.{Glm, LogisticGlm}
import breeze.linalg._
val y = DenseVector(1.0,0.0,1.0,0.0)
val X = DenseMatrix((1.0,1.5),(1.5,2.0),(2.0,1.5),(2.0,1.0))
val glm = Glm(y,X,List("V1","V2"),LogisticGlm)
glm.coefficients
glm.fitted
glm.predict().fitted
glm.predict(response=true).fitted
glm.summary
glm.plots


// Poisson regression
import scalaglm.{Glm, PoissonGlm}
import breeze.linalg._
val y = DenseVector(1.0,3.0,4.0,2.0)
val X = DenseMatrix((1.0,1.5),(1.5,2.0),(2.0,1.5),(2.0,1.0))
val glm = Glm(y,X,List("V1","V2"),PoissonGlm)
glm.coefficients
glm.summary
glm.plots


// eof

