/*
Predict.scala

Model prediction classes, for both Lm and Glm

 */


package scalaglm

import breeze.linalg._
import breeze.numerics._


trait Predict {
  val fitted: DenseVector[Double]
  val se: DenseVector[Double]
} // trait Predict


/**
  * Prediction from a fitted linear (Lm) model
  * 
  * @param mod fitted linear model
  * @param newX covariate matrix for predictions
  * 
  * @return An object of type PredictLm with several useful attributes,
  * including .fitted and .se
  */
case class PredictLm(mod: Lm, newX: DenseMatrix[Double]) extends Predict {
  require(newX.cols == mod.Xmat.cols)

  /**
    * New covariate matrix (including intercept, if required)
    */
  val nX = if (mod.addIntercept) DenseMatrix.horzcat(
    DenseVector.ones[Double](newX.rows).toDenseMatrix.t, newX)
  else newX

  /** 
    * fitted values
    */
  val fitted = nX * mod.coefficients

  /** 
    * for internal use (probably should be marked private)
    */
  lazy val xrti = nX * mod.ri

  /** 
    * standard errors for the predictions
    */
  lazy val se = norm(xrti(*, ::)) * mod.rse
} // case class PredictLm


/**
  * Prediction from a fitted linear (Glm) model
  * 
  * @param mod fitted generalised linear model
  * @param newX covariate matrix for predictions
  * @param response predictions on the response scale?
  * 
  * @return An object of type PredictGlm with several useful attributes,
  * including .fitted and .se
  */
case class PredictGlm(mod: Glm, newX: DenseMatrix[Double], response: Boolean) extends Predict {
  require(newX.cols == mod.Xmat.cols)

  /** 
    * fitted values
    */
  val nX = if (mod.addIntercept) DenseMatrix.horzcat(
    DenseVector.ones[Double](newX.rows).toDenseMatrix.t, newX)
  else newX

  /** 
    * fitted values on the linear predictor scale
    */
  val lp = nX * mod.coefficients

  /** 
    * fitted values on the desired scale
    */
  val fitted = if (response) (lp map mod.fam.bp) else lp

  /** 
    * for internal use (probably should be marked private)
    */
  lazy val rtix = nX * mod.ri

  /**
    * standard errors on the linear predictor scale
    */
  lazy val selp = norm(rtix(*, ::))

  /** 
    * standard errors for the predictions on the desired scale
    */
  lazy val se = if (response) (selp *:* (lp map (mod.fam.bpp))) else selp
} // case class PredictGlm


// eof


