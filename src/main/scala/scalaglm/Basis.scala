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
    DenseMatrix.tabulate(x.length, n)((i, j) => cosine(j+1, (x(i)-mn)/(mx-mn)))
  }

  private val r2 = math.sqrt(2.0)

  /**
    * Cosine orthogonal basis function.
    * Normalised with sqrt(2).
    *
    * @param j The order of the basis function (assumed >= 1).
    * @param x The argument of the cosine function, nominally between 0 and 1.
    * @return The value of the cosine basis function at `x`.
    */
  def cosine(j: Int, x: Double): Double = r2*math.cos(j*math.Pi*x)

  // TODO: B-spline basis
  def bs(x: DenseVector[Double], degree: Int, intKnots: DenseVector[Double]): DenseMatrix[Double] = ???

}


// eof

