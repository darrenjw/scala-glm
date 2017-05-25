/*
utils-tests.scala

Test code for utils

 */

package scalaglm

import org.scalatest.FlatSpec
import breeze.linalg._
import breeze.numerics._

class UtilsSpec extends FlatSpec {

  import Utils._

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

  it should "invert correctly (3x3)" in {
    val A = DenseMatrix((1,2,3),(0,4,5),(0,0,6)) map (_.toDouble)
    val x = DenseVector(7.0,8.0,9.0)
    val y = A * x
    val xx = backSolve(A,y)
    assert (norm(x-xx) < 0.00001)
  }

  it should "invert correctly (3x3) with multiple RHSs" in {
    val A = DenseMatrix((1,2,3),(0,4,5),(0,0,6)) map (_.toDouble)
    val x = DenseMatrix((7,8),(9,10),(11,12)) map (_.toDouble)
    val y = A * x
    val xx = backSolve(A,y)
    assert (sum((x-xx) *:* (x-xx)) < 0.00001)
  }

  "forwardSolve" should "invert correctly (1)" in {
    val A = DenseMatrix((4,0),(1,2)) map (_.toDouble)
    val x = DenseVector(3.0,-2.0)
    val y = A * x
    val xx = forwardSolve(A,y)
    assert (norm(x-xx) < 0.00001)
  }

  "bdm2aa" should "round trip with aa2bdm" in {
    val x = DenseMatrix((7,8),(9,10),(11,12)) map (_.toDouble)
    val xx = aa2bdm(bdm2aa(x))
    assert (sum((x-xx) *:* (x-xx)) < 0.00001)
  }



}
