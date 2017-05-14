/*
irls.scala

Increasingly fast, memory efficient, and numerically stable
solutions of the IRLS problem for one-parameter GLMs

 */

import breeze.linalg._
import breeze.stats.distributions.{ Gaussian, Binomial }
import breeze.numerics._
import com.github.fommil.netlib.BLAS.{ getInstance => blas }

object IRLS {

  def backSolve(A: DenseMatrix[Double],
    y: DenseVector[Double]): DenseVector[Double] = {
    val yc = y.copy
    blas.dtrsv("U", "N", "N", A.cols, A.toArray,
      A.rows, yc.data, 1)
    yc
  }

  def time[A](f: => A) = {
    val s = System.nanoTime
    val ret = f
    println("time: "+(System.nanoTime-s)/1e6+"ms")
    ret
  }

  @annotation.tailrec
  def IRLS1(
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
    val W = diag(eta map bpp) // !! Uses big diagonal matrix
    val z = y - (eta map bp)
    val bhat = bhat0 + (X.t * W * X) \ (X.t * z)
    if (norm(bhat - bhat0) < tol) bhat else IRLS1(bp, bpp, y, X, bhat, its - 1, tol)
  }

  @annotation.tailrec
  def IRLS2(
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
    val W = eta map bpp
    val z = y - (eta map bp)
    val bhat = bhat0 + (X.t * (X(::,*) * W)) \ (X.t * z) // !! naive solve
    if (norm(bhat - bhat0) < tol) bhat else IRLS2(bp, bpp, y, X, bhat, its - 1, tol)
  }

  @annotation.tailrec
  def IRLS3(
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
    val Xs = X(::,*) * sW
    val QR = qr.reduced(Xs)
    val bhat = bhat0 + backSolve(QR.r, QR.q.t * zs)
    if (norm(bhat - bhat0) < tol) bhat else IRLS3(bp, bpp, y, X, bhat, its - 1, tol)
  }
  // TODO: compute approx SEs, etc.

  // Now logistic regression
  def logReg(
    y: DenseVector[Double],
    X: DenseMatrix[Double],
    its: Int = 30
  ): DenseVector[Double] = {
    def bp(x: Double): Double = sigmoid(x)
    def bpp(x: Double): Double = {
      val e = math.exp(-x)
      e / ((1.0 + e) * (1.0 + e))
    }
    IRLS3(bp, bpp, y, X, DenseVector.zeros[Double](X.cols), its)
  }

  def logRegR(
    y: DenseVector[Double],
    X: DenseMatrix[Double]
  ): DenseVector[Double] = {
    val R = org.ddahl.rscala.RClient()
    R.y = y.toArray
    R.x = X(::,1).toDenseVector.toArray
    R.eval("mod = glm(y~x,family=binomial())")
    DenseVector[Double](R.evalD1("mod$coefficients"))
  }

  def main(args: Array[String]): Unit = {
    // simulate some synthetic data
    val N = 20000
    val beta = DenseVector(0.1, 0.3)
    val ones = DenseVector.ones[Double](N)
    val x = DenseVector(Gaussian(1.0, 3.0).sample(N).toArray)
    val X = DenseMatrix.vertcat(ones.toDenseMatrix, x.toDenseMatrix).t
    val theta = X * beta
    val p = theta map (sigmoid(_))
    val y = p map (pi => new Binomial(1, pi).draw) map (_.toDouble)
    val betahat = time { logReg(y, X) }
    println(betahat)
    // println(logRegR(y,X))
  }

}

// eof

