/*
timeSeries.scala

Some functions for working with (multivariate) time series

 */

package scalaglm

import breeze.linalg._
import breeze.numerics._
import breeze.stats._


/** 
  *  Utilities for multivariate time series
  * 
  * These utilities assume that the time series is sorted in a Breeze `DMD`
  * with variables/component series in columns, and observations in rows
  * with the first row corresponding to the first observation and the last
  * row to the last.
  * 
  */
object TimeSeries {

  // some basic data summaries

  // TODO: time series example

  // TODO: time series mdoc

  // TODO: time series scaladoc

  // TODO: time series tests

  /**
    * Sweep out the mean of each component series
    * 
    * @param x A multivariate time series
    * 
    * @return A mean zero time series
    */
  def meanCentre(x: DMD): DMD = {
    val xBar = mean(x(::, *)).t
    x(*, ::) - xBar
  }

  /**
    * Compute the covariance between two time series of the same length
    * 
    * @param x A time series
    * @param y Another time series
    * @param centre Do the series need to be centred, first?
    * 
    * @return Covariance matrix
    */
  def covariance(x: DMD, y: DMD, centre: Boolean = true): DMD = {
    val xMat = if (centre) meanCentre(x) else x
    val yMat = if (centre) meanCentre(y) else y
    (xMat.t * yMat)/(x.rows - 1.0)
  }

  /**
    * The variance matrix of a time series
    * 
    * @param x A time series
    * @param centre Does the time series need to be centred?
    * 
    * @return Variance matrix
    */
  def varianceMat(x: DMD, centre: Boolean = true): DMD = {
    val xMat = if (centre) meanCentre(x) else x
    covariance(xMat, xMat, centre=false)
  }

  /**
    * The correlation matrix of a time series
    * 
    * @param x A time series
    * @param centre Does the time series need to be centred?
    * 
    * @return Correlation matrix
    */
  def correlationMat(x: DMD, centre: Boolean = true): DMD = {
    val varMat = varianceMat(x, centre)
    val sd = sqrt(diag(varMat))
    val colScaled = x(*, ::) /:/ sd
    colScaled(::, *) /:/ sd
  }

  /**
    * Compute the auto-covariance of the time series at a given lag
    * 
    * @param x Time series
    * @param lag The lag at which the auto-covariance is to be computed (can be negative)
    * @param centre Does the time series need to be centred?
    * 
    * @return Auto-covariance matrix
    */
  def autocovariance(x: DMD, lag: Int, centre: Boolean = true): DMD = {
    if (lag < 0)
      autocovariance(x, -lag, centre).t
    else {
      val xMat = if (centre) meanCentre(x) else x
      covariance(xMat(0 until (x.rows - lag), ::), xMat(lag until x.rows, ::).copy,
        centre = false)
    }
  }

  /**
    * Compute a set of auto-covariance matrices
    * 
    * @param x Time series
    * @param lagMax The maximum lag required
    * @param centre Does the time series need to be centred?
    * 
    * @return List of `lagMax+1` auto-covariance matrices (from lag 0 to `lagMax`)
    */
  def autocovariances(x: DMD, lagMax: Int, centre: Boolean = true): List[DMD] = {
    val xMat = if (centre) meanCentre(x) else x
    (0 to lagMax).toList.map(autocovariance(xMat, _, centre=false))
  }


  import Utils._

  /**
    * Fit a mean-zero VAR(p) model to a time series via least squares
    * Since the model being fit is mean zero, you may want to mean-centre your data
    *
    * @param x Time series
    * @param p The order of the VAR(p) model to be fits
    * @param centre Mean-centre the time series before fitting?
    * 
    * @return The list of `p` fitted VAR matrices, together with the lower Cholesky triangle of the estimated innovation variance matrix
    */
  def fitVar(x: DMD, p: Int, centre: Boolean = true): (List[DMD], DMD) = {
    val n = x.rows
    val m = x.cols
    val xMat = if (centre) meanCentre(x) else x
    val size = n - p
    val bigX = (0 until p).toList.map(i => xMat((p-i-1) until (p-i-1+size), ::)).
      reduce(DenseMatrix.horzcat(_,_))
    val qR = qr.reduced(bigX)
    val q = qR.q
    val r = qR.r
    val xp = xMat(p until n, ::).copy
    val qtx =  q.t * xp
    val phi = backSolve(r, qtx)
    val phiL = (0 until p).toList.map(i => phi((i*m) until ((i+1)*m), ::).copy.t)
    val e = xp - q*qtx
    val r2 = qr.justR(e)
    (phiL, r2.t/math.sqrt(size-1))
  }


  /**
    * Check whether a given set of VAR matrices (such as returned by `fitVar`) correspond to a VAR(p) that is stationary
    * 
    * @param phi List of VAR matrices
    * 
    * @return Is this model stationary?
    */
  def isStat(phi: List[DMD]): Boolean = {
    val p = phi.length
    val m = phi.head.rows
    val phiConc = phi.reduce(DenseMatrix.horzcat(_, _))
    val v1phi = DenseMatrix.vertcat(phiConc,
      DenseMatrix.horzcat(DenseMatrix.eye[Double](m*(p-1)),
      DenseMatrix.zeros[Double](m*(p-1), m)))
    val me = max(abs(eig(v1phi).eigenvalues))
    (me < 1.0)
  }






}


// eof

