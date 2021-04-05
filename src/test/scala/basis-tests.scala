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

  "poly" should "work for orthog" in {
    val v = DenseVector(1.0,2.0,3.0)
    val m = poly(v, 2)
    assert(m(0,0) == -1.0)
    assert(m(1,0) == 0.0)
    assert(m(2,0) == 1.0)
    assert(m(0,1) == 1.0)
    assert(m(1,1) == -0.5)
    assert(m(2,1) == 1.0)
  }

}




// eof

