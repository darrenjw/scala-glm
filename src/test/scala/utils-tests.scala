/*
utils-tests.scala

Test code for utils

 */

package scalaglm

import org.scalatest.FlatSpec
import breeze.linalg._
import breeze.numerics._


class UtilsSpec extends FlatSpec {

  "1+2" should "be 3" in {
    assert(1+2 === 3)
  }

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





}
