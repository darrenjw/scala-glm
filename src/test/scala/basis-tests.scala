// basis-tests.scala

package scalaglm

import breeze.linalg._
import breeze.numerics._

import org.scalatest._
import flatspec._
import matchers._

class BasisSpec extends AnyFlatSpec {

  import Basis._

  "legendre" should "be correct for some examples" in {
    assert(legendre(-1.0,0) == 1.0)
    assert(legendre(0.0,0) == 1.0)
    assert(legendre(1.0,0) == 1.0)
    assert(legendre(-1.0,1) == -1.0)
    assert(legendre(0.0,1) == 0.0)
    assert(legendre(1.0,1) == 1.0)
    assert(legendre(-1.0,2) == 1.0)
    assert(legendre(0.0,2) == -0.5)
    assert(legendre(1.0,2) == 1.0)
    assert(legendre(0.5,3) == -7.0/16)
  }

  "poly" should "work for raw" in {
    val v = DenseVector(1.0,2.0,3.0)
    val m = poly(v, 2, raw=true)
    assert(m(0,0) == 1.0)
    assert(m(1,0) == 2.0)
    assert(m(2,0) == 3.0)
    assert(m(0,1) == 1.0)
    assert(m(1,1) == 4.0)
    assert(m(2,1) == 9.0)
  }

  it should "work for orthog" in {
    val v = DenseVector(1.0,2.0,3.0)
    val m = poly(v, 2)
    assert(m(0,0) == -1.0)
    assert(m(1,0) == 0.0)
    assert(m(2,0) == 1.0)
    assert(m(0,1) == 1.0)
    assert(m(1,1) == -0.5)
    assert(m(2,1) == 1.0)
  }

  "cosine" should "evaluate correctly" in {
    assert(abs(cosine(0.0, 1) - math.sqrt(2)) < 1e-5)
    assert(abs(cosine(0.5, 1) + 0.0)          < 1e-5)
    assert(abs(cosine(1.0, 1) + math.sqrt(2)) < 1e-5)
    assert(abs(cosine(0.0, 2) - math.sqrt(2)) < 1e-5)
    assert(abs(cosine(0.25, 2) - 0.0)         < 1e-5)
    assert(abs(cosine(0.5, 2) + math.sqrt(2)) < 1e-5)
    assert(abs(cosine(1.0, 2) - math.sqrt(2)) < 1e-5)
  }

  it should "create correct matrix" in {
    val x = linspace(2,4,9)
    val m = cosine(x, 3)
    assert(m.rows == 9)
    assert(m.cols == 3)
    assert(abs(m(0,0) - math.sqrt(2)) < 1e-5)
    assert(abs(m(4,0) - 0.0) < 1e-5)
    assert(abs(m(8,0) + math.sqrt(2)) < 1e-5)
    assert(abs(m(0,1) - math.sqrt(2)) < 1e-5)
    assert(abs(m(4,1) + math.sqrt(2)) < 1e-5)
    assert(abs(m(8,1) - math.sqrt(2)) < 1e-5)
  }




}




// eof

