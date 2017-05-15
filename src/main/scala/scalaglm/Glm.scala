/*
Glm.scala

One-parameter exponential GLMs, including logistic and Poisson regression

 */

package scalaglm

import breeze.linalg._
import breeze.stats.distributions.{ Gaussian, Binomial, Poisson }
import breeze.numerics._
import com.github.fommil.netlib.BLAS.{ getInstance => blas }

sealed trait GlmFamily {
  val bp: Double => Double
  val bpp: Double => Double
}

// List of supported observation models
case object LogisticGlm extends GlmFamily {
  val bp = x => sigmoid(x)
  val bpp = x => {
    val e = math.exp(-x)
    e / ((1.0 + e) * (1.0 + e))
  }
}

case object PoissonGlm extends GlmFamily {
  val bp = math.exp
  val bpp = math.exp
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
 * @return An object of type Glm with useful methods
 * providing information about the regression fit,
 * with .coefficients currently being the most useful
 */
case class Glm(y: DenseVector[Double],
  Xmat: DenseMatrix[Double], colNames: Seq[String], fam: GlmFamily,
  addIntercept: Boolean = true, its: Int = 50) {
  require(y.size == Xmat.rows)
  require(colNames.length == Xmat.cols)
  require(Xmat.rows >= Xmat.cols)
  val X = if (addIntercept) DenseMatrix.horzcat(
    DenseVector.ones[Double](Xmat.rows).toDenseMatrix.t, Xmat)
  else Xmat
  val names = if (addIntercept) "(Intercept)" :: colNames.toList
  else colNames.toList
  //import Utils._
  val coefficients = Irls.IRLS(fam.bp, fam.bpp, y, X, DenseVector.zeros[Double](X.cols), its)
}

object Irls {

  import Utils._

  @annotation.tailrec
  def IRLS(
    bp: Double => Double,
    bpp: Double => Double,
    y: DenseVector[Double],
    X: DenseMatrix[Double],
    bhat0: DenseVector[Double],
    its: Int,
    tol: Double = 0.0000001): DenseVector[Double] = if (its == 0) {
    println("WARNING: IRLS did not converge")
    bhat0
  } else {
    val eta = X * bhat0
    val sW = eta map bpp map math.sqrt
    val zs = (y - (eta map bp)) / sW
    val Xs = X(::, *) * sW
    val QR = qr.reduced(Xs)
    val bhat = bhat0 + backSolve(QR.r, QR.q.t * zs)
    if (norm(bhat - bhat0) < tol) bhat else IRLS(bp, bpp, y, X, bhat, its - 1, tol)
  }
  // TODO: compute approx SEs, etc.

}

// eof

