/*
glm-tests.scala

Test code for GLMs

 */

package scalaglm

import org.scalatest.FlatSpec
import breeze.linalg._
import breeze.numerics._


class GlmSpec extends FlatSpec {

  "Glm" should "fit a simple logistic regression model (without intercept)" in {
    val y = DenseVector(1.0,1.0,0.0,0.0,1.0)
    val x = DenseMatrix((1.0,2.0),(1.5,4.0),(1.0,2.0),(3.0,2.5),(2.5,3.0))
    val mod = Glm(y,x,List("Intercept","x"),LogisticGlm,false,its=1000)
    assert(mod.coefficients.length == 2)
  }

  it should "fit a simple logistic regression model (with intercept)" in {
    val y = DenseVector(1.0,1.0,0.0,0.0)
    val x = DenseMatrix((1.0,2.0),(2.5,4.0),(3.0,2.0),(2.0,3.5))
    val mod = Glm(y,x,List("V1","V2"),LogisticGlm,its=1000)
    assert(mod.coefficients.length == 3)
  }

  it should "fit a simple Poisson regression model (without intercept)" in {
    val y = DenseVector(1.0,2.0,3.0,2.0)
    val x = DenseMatrix((1.0,2.0),(1.0,4.5),(3.0,5.0),(2.5,3.0))
    val mod = Glm(y,x,List("Intercept","x"),PoissonGlm,false,its=1000)
    assert(mod.coefficients.length == 2)
  }

  it should "fit a simple Poisson regression model and get the same as R" in {
    val y = DenseVector(1.0,2.0,3.0,2.0)
    val x = DenseMatrix((2.0),(4.0),(5.0),(2.5))
    val mod = Glm(y,x,List("Covariate"),PoissonGlm,its=1000)
    val R = org.ddahl.rscala.RClient()
    R.y = y.toArray
    R.x = x(::,0).toDenseVector.toArray
    R.eval("mod = glm(y~x,family=poisson())")
    val rCoef = DenseVector[Double](R.evalD1("mod$coefficients"))
    assert(norm(mod.coefficients - rCoef) <= 0.000001)
  }


}

// eof


