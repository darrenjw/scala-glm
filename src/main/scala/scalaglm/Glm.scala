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
  val bp: Double => Double // derivative of b()
  val bpp: Double => Double // 2nd derivative of b()
}

// List of supported observation models
case object LogisticGlm extends GlmFamily {
  val bp = (x: Double) => sigmoid(x)
  val bpp = (x: Double) => {
    val e = math.exp(-x)
    e / ((1.0 + e) * (1.0 + e))
  }
}

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
  val irls = Irls.IRLS(fam.bp, fam.bpp, y, X, DenseVector.zeros[Double](X.cols), its)
  val coefficients = irls._1
  val q = irls._2
  val r = irls._3
  lazy val n = X.rows
  lazy val pp = X.cols
  lazy val df = n - pp
  lazy val ri = inv(r)
  lazy val xtxi = ri * (ri.t)
  lazy val se = breeze.numerics.sqrt(diag(xtxi))
  lazy val z = coefficients / se
  lazy val p = z.map (zi =>
    1.0 - Gaussian(0.0,1.0).cdf(math.abs(zi)) ).
    map (_ * 2)
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
}

object Glm {

    def apply(y: DenseVector[Double],
      Xmat: DenseMatrix[Double], fam: GlmFamily,
      addIntercept: Boolean, its: Int): Glm = {
      val p = Xmat.cols
      val names = (1 to p) map ("V%02d".format(_))
      Glm(y,Xmat,names,fam,addIntercept,its)
    }

    def apply(y: DenseVector[Double],
      Xmat: DenseMatrix[Double], fam: GlmFamily,its: Int): Glm =
      Glm(y,Xmat,fam,true,its)

    def apply(y: DenseVector[Double],
      Xmat: DenseMatrix[Double], fam: GlmFamily,
    addIntercept: Boolean): Glm =
      Glm(y,Xmat,fam,addIntercept,50)

    def apply(y: DenseVector[Double],
      Xmat: DenseMatrix[Double], fam: GlmFamily): Glm =
      Glm(y,Xmat,fam,true,50)

  } // object Glm


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

}

// eof

