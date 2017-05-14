/*
reg-test.scala
Test code for regression modelling

 */

import org.scalatest.FlatSpec
import breeze.linalg._
import breeze.numerics._

import Regression._

class RegSpec extends FlatSpec {

  "1+1" should "be 2" in {
    assert(1+1 === 2)
  }

  "backSolve" should "invert correctly (1)" in {
    val A = DenseMatrix((4,1),(0,2)) map (_.toDouble)
    val x = DenseVector(3.0,-2.0)
    val y = A * x
    val xx = backSolve(A,y)
    assert (norm(x-xx) < 0.00001)
  }

  it should "invert correctly (2)" in {
    val A = DenseMatrix((42,11),(0,8)) map (_.toDouble)
    val x = DenseVector(7.0,-3.0)
    val y = A * x
    val xx = backSolve(A,y)
    assert (norm(x-xx) < 0.00001)
  }

  "Lm" should "handle 2 points on a horizontal line" in {
    val y = DenseVector(5.0,5.0)
    val x = DenseMatrix((1.0,2.0),(1.0,4.0))
    val mod = Lm(y,x,List("Intercept","x"))
    val beta = DenseVector(5.0,0.0)
    assert(norm(mod.coefficients - beta) < 0.00001)
  }

  it should "handle 2 points on a slope" in {
    val y = DenseVector(2.0,3.0)
    val x = DenseMatrix((1.0,2.0),(1.0,4.0))
    val mod = Lm(y,x,List("Intercept","x"))
    val beta = DenseVector(1.0,0.5)
    assert(norm(mod.coefficients - beta) < 0.00001)
    assert(abs(mod.rSquared - 1.0) < 0.00001)
  }

  it should "handle 3 points on a diagonal" in {
    val y = DenseVector(4.0,5.0,6.0)
    val x = DenseMatrix((1.0,2.0),(1.0,3.0),(1.0,4.0))
    val mod = Lm(y,x,List("Intercept","x"))
    val beta = DenseVector(2.0,1.0)
    assert(norm(mod.coefficients - beta) < 0.00001)
    assert(abs(mod.rSquared - 1.0) < 0.00001)
  }

}

// eof


