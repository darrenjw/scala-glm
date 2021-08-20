/*
Lm.scala

Linear regression and associated diagnostics

*/

package scalaglm

import breeze.linalg._

import breeze.stats.distributions.Rand.VariableSeed.randBasis

trait Model {
  val X: DenseMatrix[Double]
  val names: Seq[String]
  val coefficients: DenseVector[Double]
}


/**
 * Linear regression modelling
 *
 * @param y Vector of responses
 * @param Xmat Covariate matrix
 * @param colNames List of covariate names
 * @param addIntercept Add an intercept term to the covariate matrix?
 *
 * @return An object of type Lm with many useful attributes
 * providing information about the regression fit
 */
case class Lm(y: DenseVector[Double],
  Xmat: DenseMatrix[Double], colNames: Seq[String], addIntercept: Boolean = true) extends Model {
  require(y.size == Xmat.rows)
  require(colNames.length == Xmat.cols)
  require(Xmat.rows >= Xmat.cols)

  /** 
    * Design matrix (including intercept, if required)
    */
  val X = if (addIntercept) DenseMatrix.horzcat(
    DenseVector.ones[Double](Xmat.rows).toDenseMatrix.t, Xmat)
  else Xmat

  /** 
    * Column names (including intercept)
    */
  val names = if (addIntercept) "(Intercept)" :: colNames.toList
  else colNames.toList

  import Utils._

  /** 
    * Breeze QR object for the design matrix
    */
  val QR = qr.reduced(X)

  /** 
    * n x p Q-matrix
    */
  val q = QR.q

  /** 
    * p x p upper-triangular R-matrix
    */
  val r = QR.r

  /**  
    * Q'y
    */
  val qty = q.t * y

  /** 
    * Fitted regression coefficients
    */
  val coefficients = backSolve(r, qty)

  import breeze.stats._

  /** 
    * Fitted values
    */
  lazy val fitted = q * qty

  /** 
    * Residuals
    */
  lazy val residuals = y - fitted

  /** 
    * Number of observations
    */
  lazy val n = X.rows

  /** 
    * Number of variables (including any intercept)
    */
  lazy val pp = X.cols

  /** 
    * Degrees of freedom
    */
  lazy val df = n - pp

  /** 
    * Residual sum of squares
    */
  lazy val rss = sum(residuals ^:^ 2.0)

  /** 
    * Residual squared error
    */
  lazy val rse = math.sqrt(rss / df)

  /** 
    * The inverse of the R-matrix
    */
  lazy val ri = inv(r)

  /**  
    * Standard errors for the regression coefficients
    */
  lazy val se = norm(ri(*,::)) * rse

  /** 
    * t-statistics for the regression coefficients
    */
  lazy val t = coefficients / se

  /** 
    * p-values for the regression coefficients
    */
  lazy val p = t.map { 1.0 - tCDF(_, df) }.map { _ * 2 }

  /** 
    * The mean of the observations
    */
  lazy val ybar = mean(y)

  /** 
    The centred observations
    */
  lazy val ymyb = y - ybar

  /** 
    The sum-of-squares of the centred observations
    */
  lazy val ssy = sum(ymyb ^:^ 2.0)

  /** 
    The R^2 value for the regression analysis
    */
  lazy val rSquared = (ssy - rss) / ssy

  /** 
    * The adjusted R^2 value for the regression
    */
  lazy val adjRs = 1.0 - ((n - 1.0) / (n - pp)) * (1 - rSquared)

  /** 
    * Degrees of freedom for the F-statistic 
    */
  lazy val k = pp - 1

  /** 
    * The f-statistic for the regression analysis
    */
  lazy val f = (ssy - rss) / k / (rss / df)

  /** 
    * The p-value associated with the f-statistic
    */
  lazy val pf = 1.0 - fCDF(f, k, df)

  /** 
    * Prints a human-readable regression summary to the console
    */
  def summary: Unit = {
    println(
      "Estimate\t S.E.\t t-stat\tp-value\t\tVariable")
    println(
      "---------------------------------------------------------")
    (0 until pp).foreach(i => printf(
      "%8.4f\t%6.3f\t%6.3f\t%6.4f %s\t%s\n",
      coefficients(i), se(i), t(i), p(i),
      if (p(i) < 0.05) "*" else " ",
      names(i)))
    printf(
      "\nResidual standard error: %8.4f on %d degrees of freedom\n",
      rse, df)
    printf(
      "Multiple R-squared: %6.4f, Adjusted R-squared: %6.4f\n",
      rSquared, adjRs)
    printf(
      "F-statistic: %6.4f on %d and %d DF, p-value: %6.5f\n\n",
      f, k, df, pf)
  }

  /** 
    * Predictions for a new matrix of covariates
    * 
    * @param newX New matrix of covariates
    * 
    * @return Prediction object
    */
  def predict(newX: DenseMatrix[Double] = Xmat): PredictLm =
    PredictLm(this, newX)

  import breeze.plot._
  def plots: Figure = {
    val fig = Figure("Linear regression diagnostics")
    val p0 = fig.subplot(2,2,0) // Obs against fitted
    p0 += plot(fitted,y,'+')
    p0 += plot(fitted,fitted)
    p0.title = "Observations against fitted values"
    p0.xlabel = "Fitted value"
    p0.ylabel = "Observation"
    val p1 = fig.subplot(2,2,1) // Studentised against fitted
    p1 += plot(fitted,studentised,'+')
    p1 += plot(fitted,DenseVector.fill(n)(0.0))
    p1.title = "Studentised residuals against fitted values"
    p1.xlabel = "Fitted value"
    p1.ylabel = "Studentised residual"
    val p2 = fig.subplot(2,2,2) // Histogram of studentised
    p2 += breeze.plot.hist(studentised)
    p2.title = "Histogram of studentised residuals"
    p2.xlabel = "Studentised residual"
    p2.ylabel = "Frequency"
    val p3 = fig.subplot(2,2,3) // Residual Q-Q plot
    val sorted = studentised.toArray.sorted
    val grid = linspace(1.0/n,1.0-1.0/n,n)
    import breeze.stats.distributions.Gaussian
    val quantiles = grid map {Gaussian(0.0,1.0).inverseCdf(_)}
    p3 += plot(quantiles, sorted)
    p3 += plot(quantiles, quantiles)
    p3.title = "Residual Q-Q plot"
    p3.xlabel = "Standard normal quantiles"
    p3.ylabel = "Studentised residuals"
    fig
  }

  /** 
    * Square root of the leverage vector
    */
  lazy val sh = norm(q(*,::))

  /** 
    * Vector containing the leverages (diagonal of the hat matrix)
    */
  lazy val h = sh * sh

  import breeze.numerics.sqrt
  lazy val studentised = residuals / sqrt(1.0 - h) / rse

} // case class Lm

  object Lm {

    /** 
      * Constructor without a name list
      */
    def apply(y: DenseVector[Double],
  Xmat: DenseMatrix[Double], addIntercept: Boolean): Lm = {
      val p = Xmat.cols
      val names = (1 to p) map ("V%02d".format(_))
      Lm(y,Xmat,names,addIntercept)
    }

    /** 
      * Constructor without a name list or addIntercept option
      */
    def apply(y: DenseVector[Double],
      Xmat: DenseMatrix[Double]): Lm =
      Lm(y,Xmat,true)

  } // object Lm




// eof

