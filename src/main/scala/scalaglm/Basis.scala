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



  // TODO: cosine series


  // TODO: B-spline basis

}


// eof

