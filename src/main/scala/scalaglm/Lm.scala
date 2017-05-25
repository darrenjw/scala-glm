/*
Lm.scala

Linear regression and associated diagnostics

*/

package scalaglm

import breeze.linalg._
import com.github.fommil.netlib.BLAS.{ getInstance => blas }

/**
 * Linear regression modelling
 *
 * @param y Vector of responses
 * @param Xmat Covariate matrix
 * @param colNames List of covariate names
 * @param addIntercept Add an intercept term to the covariate matrix?
 *
 * @return An object of type Lm with many useful methods
 * providing information about the regression fit
 */
case class Lm(y: DenseVector[Double],
  Xmat: DenseMatrix[Double], colNames: Seq[String], addIntercept: Boolean = true) {
  require(y.size == Xmat.rows)
  require(colNames.length == Xmat.cols)
  require(Xmat.rows >= Xmat.cols)
  val X = if (addIntercept) DenseMatrix.horzcat(
    DenseVector.ones[Double](Xmat.rows).toDenseMatrix.t, Xmat)
  else Xmat
  val names = if (addIntercept) "(Intercept)" :: colNames.toList
  else colNames.toList
  import Utils._
  val QR = qr.reduced(X)
  val q = QR.q
  val r = QR.r
  val qty = q.t * y
  val coefficients = backSolve(r, qty)
  import breeze.stats._
  lazy val fitted = q * qty
  lazy val residuals = y - fitted
  lazy val n = X.rows
  lazy val pp = X.cols
  lazy val df = n - pp
  lazy val rss = sum(residuals ^:^ 2.0)
  lazy val rse = math.sqrt(rss / df)
  lazy val ri = inv(r)
  lazy val xtxi = ri * (ri.t)
  lazy val se = breeze.numerics.sqrt(diag(xtxi)) * rse
  lazy val t = coefficients / se
  lazy val p = t.map { 1.0 - tCDF(_, df) }.map { _ * 2 }
  lazy val ybar = mean(y)
  lazy val ymyb = y - ybar
  lazy val ssy = sum(ymyb ^:^ 2.0)
  lazy val rSquared = (ssy - rss) / ssy
  lazy val adjRs = 1.0 - ((n - 1.0) / (n - pp)) * (1 - rSquared)
  lazy val k = pp - 1
  lazy val f = (ssy - rss) / k / (rss / df)
  lazy val pf = 1.0 - fCDF(f, k, df)
  def summary: Unit = {
    println(
      "Estimate\t S.E.\t t-stat\tp-value\t\tVariable")
    println(
      "---------------------------------------------------------")
    (0 until pp).foreach(i => printf(
      "%8.4f\t%6.3f\t%6.3f\t%6.4f %s\t%s\n",
      coefficients(i), se(i), t(i), p(i),
      if (p(i) < 0.05) "*" else " ",
      names(i)))
    printf(
      "\nResidual standard error: %8.4f on %d degrees of freedom\n",
      rse, df)
    printf(
      "Multiple R-squared: %6.4f, Adjusted R-squared: %6.4f\n",
      rSquared, adjRs)
    printf(
      "F-statistic: %6.4f on %d and %d DF, p-value: %6.5f\n\n",
      f, k, df, pf)
  }

} // case class Lm

  object Lm {

    def apply(y: DenseVector[Double],
  Xmat: DenseMatrix[Double], addIntercept: Boolean): Lm = {
      val p = Xmat.cols
      val names = (1 to p) map ("V%02d".format(_))
      Lm(y,Xmat,names,addIntercept)
    }

    def apply(y: DenseVector[Double],
      Xmat: DenseMatrix[Double]): Lm =
      Lm(y,Xmat,true)

  } // object Lm



object Utils {

  /**
   * Backsolve an upper-triangular linear system
   * with a single RHS
   *
   * @param A An upper-triangular matrix
   * @param y A single vector RHS
   *
   * @return The solution, x, of the linear system A x = y
   */
  def backSolve(A: DenseMatrix[Double],
    y: DenseVector[Double]): DenseVector[Double] = {
    val yc = y.copy
    blas.dtrsv("U", "N", "N", A.cols, A.toArray,
      A.rows, yc.data, 1)
    yc
  }

  import org.apache.commons.math3.special.Beta

  def tCDF(t: Double, df: Double): Double = {
    val xt = df / (t * t + df)
    1.0 - 0.5 * Beta.regularizedBeta(xt, 0.5 * df, 0.5)
  }

  def fCDF(x: Double, d1: Double, d2: Double) = {
    val xt = x * d1 / (x * d1 + d2)
    Beta.regularizedBeta(xt, 0.5 * d1, 0.5 * d2)
  }

  def time[A](f: => A) = {
    val s = System.nanoTime
    val ret = f
    println("time: "+(System.nanoTime-s)/1e6+"ms")
    ret
  }










  // Example main runner method
  def main(args: Array[String]): Unit = {

    val url = "http://archive.ics.uci.edu/ml/machine-learning-databases/00291/airfoil_self_noise.dat"
    val fileName = "self-noise.csv"

    // download the file to disk if it hasn't been already
    val file = new java.io.File(fileName)
    if (!file.exists) {
      val s = new java.io.PrintWriter(file)
      val data = scala.io.Source.fromURL(url).getLines
      data.foreach(l => s.write(l.trim.split('\t').filter(_ != "").mkString("", ",", "\n")))
      s.close
    }

    // read the file from disk
    val mat = csvread(new java.io.File(fileName))
    println("Dim: " + mat.rows + " " + mat.cols)
    val y = mat(::, 5) // response is the final column
    val X = mat(::, 0 to 4)
    val mod = Lm(y, X, List("Freq", "Angle", "Chord", "Velo", "Thick"))
    mod.summary

    // test without name list
    Lm(y,X,false).summary
    Lm(y,X).summary

  } // main

}

// eof

