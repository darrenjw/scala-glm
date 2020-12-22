/*
lm-tests.scala

Test code for regression modelling

 */

package scalaglm

import breeze.linalg._
import breeze.numerics._

import org.scalatest._
import flatspec._
import matchers._

class LmSpec extends AnyFlatSpec {

  import Utils._

  "Lm" should "handle 2 points on a horizontal line (manual intercept)" in {
    val y = DenseVector(5.0,5.0)
    val x = DenseMatrix((1.0,2.0),(1.0,4.0))
    val mod = Lm(y,x,List("Intercept","x"),false)
    val beta = DenseVector(5.0,0.0)
    assert(norm(mod.coefficients - beta) < 0.00001)
  }

  it should "handle 2 points on a slope (manual intercept)" in {
    val y = DenseVector(2.0,3.0)
    val x = DenseMatrix((1.0,2.0),(1.0,4.0))
    val mod = Lm(y,x,List("Intercept","x"),false)
    val beta = DenseVector(1.0,0.5)
    assert(norm(mod.coefficients - beta) < 0.00001)
    assert(abs(mod.rSquared - 1.0) < 0.00001)
  }

  it should "handle 3 points on a diagonal (manual intercept)" in {
    val y = DenseVector(4.0,5.0,6.0)
    val x = DenseMatrix((1.0,2.0),(1.0,3.0),(1.0,4.0))
    val mod = Lm(y,x,List("Intercept","x"),false)
    val beta = DenseVector(2.0,1.0)
    assert(norm(mod.coefficients - beta) < 0.00001)
    assert(abs(mod.rSquared - 1.0) < 0.00001)
  }

  it should "handle 2 points on a horizontal line (auto intercept)" in {
    val y = DenseVector(5.0,5.0)
    val x = DenseMatrix((2.0),(4.0))
    val mod = Lm(y,x,List("x"))
    val beta = DenseVector(5.0,0.0)
    assert(norm(mod.coefficients - beta) < 0.00001)
  }

  it should "handle 2 points on a slope (auto intercept)" in {
    val y = DenseVector(2.0,3.0)
    val x = DenseMatrix((2.0),(4.0))
    val mod = Lm(y,x,List("x"))
    val beta = DenseVector(1.0,0.5)
    assert(norm(mod.coefficients - beta) < 0.00001)
    assert(abs(mod.rSquared - 1.0) < 0.00001)
  }

  it should "handle 3 points on a diagonal (auto intercept)" in {
    val y = DenseVector(4.0,5.0,6.0)
    val x = DenseMatrix((2.0),(3.0),(4.0))
    val mod = Lm(y,x,List("x"))
    val beta = DenseVector(2.0,1.0)
    assert(norm(mod.coefficients - beta) < 0.00001)
    assert(abs(mod.rSquared - 1.0) < 0.00001)
  }

  it should "fit a simple linear regression model and get the same as R" in {
    val y = DenseVector(1.0,2.5,0.5,3.0)
    val x = DenseMatrix((1.0),(2.5),(3.0),(2.0))
    val mod = Lm(y,x,List("Covariate"))
    //mod.summary
    val R = org.ddahl.rscala.RClient()
    R.eval("y = %-", y.toArray)
    R.eval("x = %-", x(::,0).toDenseVector.toArray)
    R.eval("mod = lm(y~x)")
    val rCoef = DenseVector[Double](R.evalD1("mod$coefficients"))
    assert(norm(mod.coefficients - rCoef) <= 0.00001)
    val rSe = DenseVector[Double](R.evalD1("summary(mod)$coefficients[,2]"))
    assert(norm(mod.se - rSe) <= 0.00001)
    val rT = DenseVector[Double](R.evalD1("summary(mod)$coefficients[,3]"))
    assert(norm(mod.t - rT) <= 0.00001)
    val rP = DenseVector[Double](R.evalD1("summary(mod)$coefficients[,4]"))
    assert(norm(mod.p - rP) <= 0.00001)
    val rF = DenseVector[Double](R.evalD1("mod$fitted.values"))
    assert(norm(mod.fitted - rF) <= 0.0001)
    val rStud = DenseVector[Double](R.evalD1("rstandard(mod)"))
    assert(norm(mod.studentised - rStud) <= 0.0001)
    val rPred = DenseVector[Double](R.evalD1("predict(mod)"))
    assert(norm(mod.predict().fitted - rPred) <= 0.0001)
    val rPredSe = DenseVector[Double](R.evalD1("predict(mod,se.fit=TRUE)$se.fit"))
    assert(norm(mod.predict().se - rPredSe) <= 0.0001)
  }








}

// eof


