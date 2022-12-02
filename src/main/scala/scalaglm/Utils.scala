/* 
utils.scala


 */

package scalaglm

import breeze.linalg._
import breeze.numerics._
import breeze.signal._
import breeze.math._
import math.Pi

object Utils {

  import dev.ludovic.netlib.blas.BLAS.{ getInstance => blas }

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

  /** 
    * Backsolve an upper-triangular linear system
    * with multiple RHSs
    * 
    * @param A An upper-triangular matrix
    * @param Y A matrix with columns corresponding to RHSs
    * 
    * @return Matrix of solutions, X, to the linear system A X = Y
    */
  def backSolve(A: DenseMatrix[Double], Y: DenseMatrix[Double]): DenseMatrix[Double] = {
    val yc = Y.copy
    blas.dtrsm("L", "U", "N", "N", yc.rows, yc.cols, 1.0, A.toArray, A.rows, yc.data, yc.rows)
    yc
  }

  /** 
    * Forward solve a lower-triangular linear system
    * with a single RHS
    * 
    * @param A A lower-triangular matrix
    * @param y A single vector RHS
    *
    * @return The solution, x, of the linear system A x = y 
    */
  def forwardSolve(A: DenseMatrix[Double],
    y: DenseVector[Double]): DenseVector[Double] = {
    val yc = y.copy
    blas.dtrsv("L", "N", "N", A.cols, A.toArray, A.rows, yc.data, 1)
    yc
  }

  /** 
    * Breeze DenseMatrix to Array[Array] conversion
    * 
    * Useful for sending matrices to R via rscala
    * 
    * @param m A Breeze DenseMatrix
    * 
    * @return The matrix as an array of arrays (row-major)
    */
  def bdm2aa(m: DenseMatrix[Double]): Array[Array[Double]] =
    (0 until m.rows).toArray map (i => m(i,::).t.toArray)

  /** 
    * Array[Array] to Breeze DenseMatrix conversion
    * 
    * Useful for unpacking matrices which have come back to Scala
    * from R via rscala
    * 
    * @param a A matrix represented as an array of arrays (row-major)
    * 
    * @return A Breeze DenseMatrix representation of the matrix
    */
  def aa2bdm(a: Array[Array[Double]]): DenseMatrix[Double] = {
    val r = a.length
    val c = a(0).length
    val m = DenseMatrix.zeros[Double](r,c)
    (0 until r).foreach(i => { m(i,::) := DenseVector(a(i)).t })
    m
  }

  /**
    *  Discrete cosine transform (DCT) of a DenseVector
    * 
    *  Normalised by a factor of 2/N
    * 
    * @param x An input vector
    * 
    * @return The normalised DCT-II of the input
    */
  def dct(x: DenseVector[Double]): DenseVector[Double] = {
    val N = x.length
    val y = DenseVector.vertcat(x, x(N-1 to 0 by -1))
    val Y = fourierTr(y)
    val ff = DenseVector.tabulate(2*N){k => -Pi*i*k/(2*N)}
    val sY = Y *:* exp(ff)
    sY(0 to N-1).map(_.real) / N.toDouble
  }

  /**
    * Inverse discrete cosine transform (DCT) of a DenseVector
    * 
    * Exactly inverts `dct`
    * 
    * @param x An input vector containing a transform
    * 
    * @return The (unnormalised) DCT-III of the input
    */
  def idct(x: DenseVector[Double]): DenseVector[Double] = {
    val N = x.length
    val y = DenseVector.vertcat(x, DenseVector(0.0), -x(N-1 to 1 by -1))
    val ff = DenseVector.tabulate(2*N){k => Pi*i*k/(2*N)}
    val sy = y.map(Complex(_,0.0)) *:* exp(ff)
    val Y = iFourierTr(sy)
    Y(0 to N-1).map(_.real) * N.toDouble
  }

  /**
    * Discrete cosine transform (DCT) of a DenseMatrix
    * 
    * DCT applied to rows and columns
    * 
    * @param X An input matrix
    * @param inverse Inverse or forward transform. Defaults to `false`
    * 
    * @return The normalised DCT-II of the input
    */
  def dct2(X: DenseMatrix[Double], inverse: Boolean = false): DenseMatrix[Double] = {
    val x = X.copy
    (0 until x.rows).foreach{j =>
      x(j, ::) := (if (inverse) idct(x(j, ::).t)
        else dct(x(j, ::).t)).t}
    (0 until x.cols).foreach{k =>
      x(::, k) := (if (inverse) idct(x(::, k))
        else dct(x(::, k)))}
    x
  }

  /**
    * dct2(x, true)
    */
  def idct2(x: DenseMatrix[Double]): DenseMatrix[Double] = dct2(x, true)

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


  import breeze.plot._
  /** 
    * pairs plot
    * 
    * @param mat a matrix with variables in columns and observations in rows
    * @param names a sequence of variable names
    * 
    * @return The breeze-viz Figure object
    */
  def pairs(mat: DenseMatrix[Double], names: Seq[String]): Figure = {
    require(mat.cols == names.length)
    val fig = Figure("Scatterplot matrix")
    val p = mat.cols
    (0 until p).foreach{i =>
       (0 until p).foreach {j =>
          val pij = fig.subplot(p,p,i*p+j)
          if (i == j) {
            pij += hist(mat(::,i))
            pij.title = names(i)
            pij.xlabel = names(i)
          } else {
            pij += plot(mat(::,j),mat(::,i),'.')
            pij.xlabel = names(j)
            pij.ylabel = names(i)
          }
        }
      }
    fig
  }

  /** 
    * pairs plot (for unlabeled variables)
    * 
    * @param mat a matrix with variables in columns and observations in rows
    * 
    * @return The breeze-viz Figure object
    */
  def pairs(mat: DenseMatrix[Double]): Figure = {
    val names = (1 to mat.cols) map ("V%02d".format(_))
    pairs(mat, names)
  }




} // object Utils
