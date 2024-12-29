/*
utils-pbts.scala

Some property-based test code for utils

 */

package scalaglm

import breeze.linalg._
import breeze.numerics._
import math.abs

import org.scalatest._
import flatspec._
import matchers._
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class UtilsPbts
    extends AnyFlatSpec
    with should.Matchers
    with ScalaCheckPropertyChecks {

  import Utils._

  forAll { (x0: Double, x1: Double, x2: Double) =>
    whenever(abs(x0) < 1e80 && abs(x1) < 1e80 && abs(x2) < 1e80) {

      val A = DenseMatrix((1, 2, 3), (0, 4, 5), (0, 0, 6)) map (_.toDouble)
      val x = DenseVector(x0, x1, x2)
      val y = A * x
      val xx = backSolve(A, y)
      norm(x - xx) < 1e-5 + 1e-5 * norm(x) shouldBe true
    }
  }

  // TODO: add forward solve and multiple RHSs

}
