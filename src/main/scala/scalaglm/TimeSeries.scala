/*
timeSeries.scala

Some functions for working with (multivariate) time series

 */

package scalaglm

import breeze.linalg._
import breeze.numerics._
import breeze.stats._


object TimeSeries {

  // some basic data summaries

  // TODO: time series example

  // TODO: time series mdoc

  // TODO: time series scaladoc

  // TODO: time series tests

  def meanCentre(x: DMD): DMD = {
    val xBar = mean(x(::, *)).t
    x(*, ::) - xBar
  }

  def covariance(x: DMD, y: DMD, centre: Boolean = true): DMD = {
    val xMat = if (centre) meanCentre(x) else x
    val yMat = if (centre) meanCentre(y) else y
    (xMat.t * yMat)/(x.rows - 1.0)
  }

  def varianceMat(x: DMD, centre: Boolean = true): DMD = {
    val xMat = if (centre) meanCentre(x) else x
    covariance(xMat, xMat, centre=false)
  }

  def correlationMat(x: DMD, centre: Boolean = true): DMD = {
    val varMat = varianceMat(x, centre)
    val sd = sqrt(diag(varMat))
    val colScaled = x(*, ::) /:/ sd
    colScaled(::, *) /:/ sd
  }

  def autocovariance(x: DMD, lag: Int, centre: Boolean = true): DMD = {
    if (lag < 0)
      autocovariance(x, -lag, centre).t
    else {
      val xMat = if (centre) meanCentre(x) else x
      covariance(xMat(0 until (x.rows - lag), ::), xMat(lag until x.rows, ::).copy,
        centre = false)
    }
  }

  def autocovariances(x: DMD, lagMax: Int, centre: Boolean = true): List[DMD] = {
    val xMat = if (centre) meanCentre(x) else x
    (0 to lagMax).toList.map(autocovariance(xMat, _, centre=false))
  }


  import Utils._

  def fitVar(x: DMD, p: Int): (List[DMD], DMD) = {
    val n = x.rows
    val m = x.cols
    val size = n - p
    val bigX = (0 until p).toList.map(i => x((p-i-1) until (p-i-1+size), ::)).
      reduce(DenseMatrix.horzcat(_,_))
    val qR = qr.reduced(bigX)
    val q = qR.q
    val r = qR.r
    val xp = x(p until n, ::).copy
    val qtx =  q.t * xp
    val phi = backSolve(r, qtx)
    val phiL = (0 until p).toList.map(i => phi((i*m) until ((i+1)*m), ::).copy.t)
    val e = xp - q*qtx
    val r2 = qr.justR(e)
    (phiL, r2.t/math.sqrt(size-1))
  }

  // this returns the list of phi matrices and the lower cholesky triangle of the estimated innovation variance matrix

  // stationarity check given autoregressive matrices
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

