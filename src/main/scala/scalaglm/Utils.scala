/* 
utils.scala


 */

package scalaglm

import breeze.linalg._

object Utils {

  import com.github.fommil.netlib.BLAS.{ getInstance => blas }

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

  /** 
    * The CDF of the t-distribution
    */
  def tCDF(t: Double, df: Double): Double = {
    val xt = df / (t * t + df)
    1.0 - 0.5 * Beta.regularizedBeta(xt, 0.5 * df, 0.5)
  }

  /** 
    * The CDF of the f-distribution
    */
  def fCDF(x: Double, d1: Double, d2: Double) = {
    val xt = x * d1 / (x * d1 + d2)
    Beta.regularizedBeta(xt, 0.5 * d1, 0.5 * d2)
  }

  /** 
    * A very simple function for timing computations - not for general use
    */
  def time[A](f: => A) = {
    val s = System.nanoTime
    val ret = f
    println("time: "+(System.nanoTime-s)/1e6+"ms")
    ret
  }


  /** 
    * Example of a main runner method - not for general use - will probably get removed
    * in due course
    */
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

} // object Utils
