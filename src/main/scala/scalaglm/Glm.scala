/*
Glm.scala

One-parameter exponential GLMs, including logistic and Poisson regression

 */

package scalaglm

import breeze.linalg._
import breeze.stats.distributions.{ Gaussian, Binomial, Poisson }
import breeze.numerics._
import com.github.fommil.netlib.BLAS.{ getInstance => blas }

/** 
  * Trait for simple one-parameter exponential family observation models.
  */
sealed trait GlmFamily {
  /** 
    * First derivative of the b(.) function
    */
  val bp: Double => Double
  /** 
    * Second derivative of the b(.) function
    */
  val bpp: Double => Double
}

// List of supported observation models

/**
  * GlmFamily object for logistic regression
  */
case object LogisticGlm extends GlmFamily {
  val bp = (x: Double) => sigmoid(x)
  val bpp = (x: Double) => {
    val e = math.exp(-x)
    e / ((1.0 + e) * (1.0 + e))
  }
}

/** 
  * GlmFamily object for Poisson regression
  */
case object PoissonGlm extends GlmFamily {
  val bp = math.exp _
  val bpp = math.exp _
}

/**
 * Generalised linear regression modelling
 *
 * @param y Vector of responses
 * @param Xmat Covariate matrix
 * @param colNames List of covariate names
 * @param fam Observation model. eg. LogisticGlm or PoissonGlm
 * @param addIntercept Add an intercept term to the covariate matrix?
 * @param its Max iterations for the IRLS algorithm (default 50)
 *
 * @return An object of type Glm with many useful methods
 * providing information about the regression fit,
 * including .coefficients, .p and .summary
 */
case class Glm(y: DenseVector[Double],
  Xmat: DenseMatrix[Double], colNames: Seq[String], fam: GlmFamily,
  addIntercept: Boolean = true, its: Int = 50) extends Model {
  require(y.size == Xmat.rows)
  require(colNames.length == Xmat.cols)
  require(Xmat.rows >= Xmat.cols)

  /** 
    * Design matrix (including the intercept column, if required)
    */
  val X = if (addIntercept) DenseMatrix.horzcat(
    DenseVector.ones[Double](Xmat.rows).toDenseMatrix.t, Xmat)
  else Xmat

  /** 
    * Sequence of variable names (including the intercept)
    */
  val names = if (addIntercept) "(Intercept)" :: colNames.toList
  else colNames.toList

  /** 
    * Tuple containing results of running the IRLS algorithm - not for general use
    */
  val irls = Irls.IRLS(fam.bp, fam.bpp, y, X, DenseVector.zeros[Double](X.cols), its)

  /** 
    * Fitted regression coefficients
    */
  val coefficients = irls._1

  /** 
    * Final Q-matrix from the IRLS algorithm
    */
  val q = irls._2

  /** 
    * Final R-matrix from the IRLS algorithm
    */
  val r = irls._3

  /** 
    * Number of observations
    */
  lazy val n = X.rows

  /** 
    * Number of variables (including the intercept)
    */
  lazy val pp = X.cols

  /** 
    * Degrees of freedom
    */
  lazy val df = n - pp

  /** 
    * Inverse of the final R-matrix
    */
  lazy val ri = inv(r)

  /** 
    * Standard errors for the regression coefficients
    */
  lazy val se = norm(ri(*, ::))

  /** 
    * z-statistics for the regression coefficients
    */
  lazy val z = coefficients / se

  /** 
    * p-values for the regression coefficients
    */
  lazy val p = z.map (zi =>
    1.0 - Gaussian(0.0,1.0).cdf(math.abs(zi)) ).
    map (_ * 2)

  /** 
    * Prints a human-readable regression summary to the console
    */
  def summary: Unit = {
    println(
      "Estimate\t S.E.\t z-stat\tp-value\t\tVariable")
    println(
      "---------------------------------------------------------")
    (0 until pp).foreach(i => printf(
      "%8.4f\t%6.3f\t%6.3f\t%6.4f %s\t%s\n",
      coefficients(i), se(i), z(i), p(i),
      if (p(i) < 0.05) "*" else " ",
      names(i)))
  }

  /** 
    * Predictions for a new matrix of covariates
    * 
    * @param newX New matrix of covariates
    * @param response Fitted values on the scale of the response?
    * 
    * @return Prediction object
    */
  def predict(newX: DenseMatrix[Double] = Xmat,
    response: Boolean = false): PredictGlm =
    PredictGlm(this, newX, response)

  lazy val fitted = predict(response = true).fitted

  import breeze.plot._
  def plots: Figure = {
    val fig = Figure("GLM regression diagnostics")
    val p0 = fig.subplot(1,1,0)
    p0 += plot(fitted,y,'+')
    p0 += plot(fitted,fitted)
    p0.title = "Observations against fitted values"
    p0.xlabel = "Fitted value"
    p0.ylabel = "Observation"
    fig
  }


} // case class Glm

object Glm {

  /** 
    * Constructor without a name list
    */
    def apply(y: DenseVector[Double],
      Xmat: DenseMatrix[Double], fam: GlmFamily,
      addIntercept: Boolean, its: Int): Glm = {
      val p = Xmat.cols
      val names = (1 to p) map ("V%02d".format(_))
      Glm(y,Xmat,names,fam,addIntercept,its)
    }

  /** 
    * Constructor without a name list or addIntercept option
    */
    def apply(y: DenseVector[Double],
      Xmat: DenseMatrix[Double], fam: GlmFamily,its: Int): Glm =
      Glm(y,Xmat,fam,true,its)

  /** 
    * Constructor without a name list or its option
    */
    def apply(y: DenseVector[Double],
      Xmat: DenseMatrix[Double], fam: GlmFamily,
    addIntercept: Boolean): Glm =
      Glm(y,Xmat,fam,addIntercept,50)

  /** 
    * Constructor without a name list, addIntercept or its option
    */
    def apply(y: DenseVector[Double],
      Xmat: DenseMatrix[Double], fam: GlmFamily): Glm =
      Glm(y,Xmat,fam,true,50)

  } // object Glm


object Irls {

  import Utils._

  /** 
    * IRLS algorithm, called by Glm
    */
  @annotation.tailrec
  def IRLS(
    bp: Double => Double,
    bpp: Double => Double,
    y: DenseVector[Double],
    X: DenseMatrix[Double],
    bhat0: DenseVector[Double],
    its: Int,
    tol: Double = 0.0000001
  ): (DenseVector[Double],DenseMatrix[Double],DenseMatrix[Double]) = {
    val eta = X * bhat0
    val sW = eta map bpp map math.sqrt
    val zs = (y - (eta map bp)) / sW
    val Xs = X(::, *) * sW
    val QR = qr.reduced(Xs)
    val bhat = bhat0 + backSolve(QR.r, QR.q.t * zs)
    if (its <= 1) println("WARNING: IRLS did not converge")
    if ((norm(bhat - bhat0) < tol)|(its <= 1))
      (bhat, QR.q, QR.r)
    else
      IRLS(bp, bpp, y, X, bhat, its - 1, tol)
  }

} // object Irls




// eof

