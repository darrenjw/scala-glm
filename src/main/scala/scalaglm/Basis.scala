/*
Basis.scala

(Orthogonal) bases for smoothing and interpolation

*/

package scalaglm

import breeze.linalg._

object Basis {

  /**
    * Construct a polynomial basis matrix with `degree` columns using
    * input vector `x`.
    * Defaults to orthogonal Legendre polynomials, but `raw` monomials can be requested.
    *
    * @param x A covariate vector.
    * @param degree The maximum degree of the polynomial basis.
    * @param raw Raw monomial basis (true) or orthogonal polynomials (false, default).
    * 
    * @return A matrix with rows matching the length of `x` and `degree` columns.
    */
  def poly(x: DenseVector[Double], degree: Int, raw: Boolean = false): DenseMatrix[Double] = {
    if (raw) {
      DenseMatrix.tabulate(x.length, degree)((i, j) => math.pow(x(i), j+1))
    } else {
      val mx = max(x)
      val mn = min(x)
      DenseMatrix.tabulate(x.length, degree)((i, j) => legendre((2*x(i)-mn-mx)/(mx-mn), j+1))
    }
  }

  /**
    * Legendre orthogonal polynomial function.
    * Evaluated using Bonnet's recursion.
    *
    * @param x The argument of the polynomial, nominally between -1 and 1.
    * @param n The degree of the polynomial.
    * 
    * @return The value of the `n`th polynomial at `x`.
    */
  def legendre(x: Double, n: Int): Double = n match {
    case 0 => 1.0
    case 1 => x
    case _ => ((2*n-1)*x*legendre(x, n-1) - (n-1)*legendre(x, n-2)) / n
  }

  /**
    * Construct a cosine series basis matrix with `n` columns using
    * input vector `x`.
    *
    * @param x A covariate vector.
    * @param n The number of cosine series basis functions required.
    * 
    * @return A matrix with rows matching the length of `x` and `n` columns.
    */
  def cosine(x: DenseVector[Double], n: Int): DenseMatrix[Double] = {
    val mx = max(x)
    val mn = min(x)
    DenseMatrix.tabulate(x.length, n)((i, j) => cosine((x(i)-mn)/(mx-mn), j+1))
  }

  private val r2 = math.sqrt(2.0)

  /**
    * Cosine orthogonal basis function.
    * Normalised with sqrt(2).
    *
    * @param x The argument of the cosine function, nominally between 0 and 1.
    * @param j The order of the basis function (assumed >= 1).
    * 
    * @return The value of the cosine basis function at `x`.
    */
  def cosine(x: Double, j: Int): Double = r2*math.cos(j*math.Pi*x)


  // TODO: B-spline basis
  def bs(x: DenseVector[Double], degree: Int = 3, intercept: Boolean = false)(
    intKnots: DenseVector[Double] = DenseVector(), lb: Double = min(x), ub: Double = max(x)
  ): DenseMatrix[Double] = ???


  // TODO: need some careful tests!
  def bspline(x: Double, i: Int, deg: Int, knots: DenseVector[Double]): Double = deg match {
    case 0 => if ((x >= knots(i))&(x < knots(i+1))) 1.0 else 0.0
    case _ => {
      val a0 = if (knots(i+deg) == knots(i)) 0.0 else (x-knots(i))/(knots(i+deg)-knots(i))
      val a1 = if (knots(i+deg+1) == knots(i+1)) 0.0 else (knots(i+deg+1)-x)/(knots(i+deg+1)-knots(i+1))
      a0*bspline(x, i, deg-1, knots) + a1*bspline(x, i+1, deg-1, knots)
    }
  }

}


// eof

