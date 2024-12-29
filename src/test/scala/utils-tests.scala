/*
utils-tests.scala

Test code for utils

 */

package scalaglm

import breeze.linalg._
import breeze.numerics._

import org.scalatest._
import flatspec._
import matchers._

class UtilsSpec extends AnyFlatSpec {

  import Utils._

  "backSolve" should "invert correctly (1)" in {
    val A = DenseMatrix((4, 1), (0, 2)) map (_.toDouble)
    val x = DenseVector(3.0, -2.0)
    val y = A * x
    val xx = backSolve(A, y)
    assert(norm(x - xx) < 0.00001)
  }

  it should "invert correctly (2)" in {
    val A = DenseMatrix((42, 11), (0, 8)) map (_.toDouble)
    val x = DenseVector(7.0, -3.0)
    val y = A * x
    val xx = backSolve(A, y)
    assert(norm(x - xx) < 0.00001)
  }

  it should "invert correctly (3x3)" in {
    val A = DenseMatrix((1, 2, 3), (0, 4, 5), (0, 0, 6)) map (_.toDouble)
    val x = DenseVector(7.0, 8.0, 9.0)
    val y = A * x
    val xx = backSolve(A, y)
    assert(norm(x - xx) < 0.00001)
  }

  it should "invert correctly (3x3) with multiple RHSs" in {
    val A = DenseMatrix((1, 2, 3), (0, 4, 5), (0, 0, 6)) map (_.toDouble)
    val x = DenseMatrix((7, 8), (9, 10), (11, 12)) map (_.toDouble)
    val y = A * x
    val xx = backSolve(A, y)
    assert(sum((x - xx) *:* (x - xx)) < 0.00001)
  }

  "forwardSolve" should "invert correctly (1)" in {
    val A = DenseMatrix((4, 0), (1, 2)) map (_.toDouble)
    val x = DenseVector(3.0, -2.0)
    val y = A * x
    val xx = forwardSolve(A, y)
    assert(norm(x - xx) < 0.00001)
  }

  "bdm2aa" should "round trip with aa2bdm" in {
    val x = DenseMatrix((7, 8), (9, 10), (11, 12)) map (_.toDouble)
    val xx = aa2bdm(bdm2aa(x))
    assert(sum((x - xx) *:* (x - xx)) < 0.00001)
  }

  import breeze.math._
  import math.Pi
  import breeze.stats.distributions._
  import breeze.stats.distributions.Rand.VariableSeed.randBasis

  def dct0(x: DenseVector[Double]): DenseVector[Double] = {
    val N = x.length
    val cf = DenseVector.tabulate(N) { n => Pi * (n + 0.5) / N }
    val X = DenseVector.tabulate(N) { k => sum(x *:* cos(cf * k.toDouble)) }
    X * (2.0 / N)
  }

  def idct0(x: DenseVector[Double]): DenseVector[Double] = {
    val N = x.length
    val cf = DenseVector.tabulate(N - 1) { k => Pi * (k + 1) / N }
    DenseVector.tabulate(N) { n =>
      x(0) / 2 + sum(x(1 to N - 1) *:* cos(cf * (n + 0.5)))
    }
  }

  val N = 20 + Poisson(20).draw()
  val x = DenseVector(Gaussian(1.0, 2.0).sample(N).toArray)
  val tol = 1e-6

  "A DCT" should "have the correct length" in {
    val xt = dct0(x)
    assert(xt.length == N)
  }

  "dct" should "agree with dct0" in {
    assert(norm(dct(x) - dct0(x)) < tol)
  }

  "idct" should "agree with idct0" in {
    assert(norm(idct(x) - idct0(x)) < tol)
  }

  "dct0" should "invert" in {
    assert(norm(idct0(dct0(x)) - x) < tol)
  }

  "dct" should "invert" in {
    assert(norm(idct(dct(x)) - x) < tol)
  }

  val M = 20 + Poisson(20).draw()
  val Mat = DenseMatrix.fill(N, M)(Gaussian(1.0, 2.0).draw())

  def mnorm(m: DenseMatrix[Double]): Double =
    norm(DenseVector(m.toArray))

  "A 2d DCT" should "have the correct dimensions" in {
    val xt = dct2(Mat)
    assert(xt.rows == N)
    assert(xt.cols == M)
  }

  "A 2d DCT" should "invert" in {
    assert(mnorm(dct2(dct2(Mat), true) - Mat) < tol)
  }

}
