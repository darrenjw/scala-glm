/*
Pca.scala

 */

package scalaglm

import breeze.linalg._
import breeze.stats._

/** Principal components analysis
  *
  * Computed using SVD of the centred data matrix rather than from the spectral
  * decomposition of the covariance matrix. eg. More like the R function
  * "prcomp" than the R function "princomp".
  *
  * NOTE: .loadings are transposed relative to the PCA function in Breeze
  *
  * @param mat
  *   Data matrix with rows corresponding to observations and columns
  *   corresponding to variables
  * @param colNames
  *   Sequence of column names of mat
  *
  * @return
  *   An object of type Pca with methods such as .loadings, .scores, .sdev and
  *   .summary
  */
case class Pca(mat: DMD, colNames: Seq[String]) {

  require(mat.cols == colNames.length)

  /** Number of observations
    */
  val n = mat.rows

  /** Number of variables
    */
  val p = mat.cols

  /** Column names (as a List)
    */
  val names = colNames.toList

  /** Column means (for centring)
    */
  val xBar = mean(mat(::, *)).t

  /** Centred data matrix
    */
  val x = mat(*, ::) - xBar

  /** Breeze SVD object for the centred data matrix
    */
  val SVD = svd.reduced(x)

  /** Loadings/rotation matrix. Note that this is the TRANSPOSE of the
    * corresponding Breeze method. But this is the usual way the rotations are
    * reported. See how the .summary method labels the rows and columns if you
    * are confused.
    */
  val loadings = SVD.Vt.t

  /** Standard deviations of the principal components
    */
  val sdev = SVD.S / math.sqrt(x.rows - 1)

  /** n x p matrix of scores - the rotated data
    */
  lazy val scores = x * loadings

  /** Variances of the principal components
    */
  lazy val variance = sdev map (x => x * x)

  /** The total variance of the principal components
    */
  lazy val totVar = sum(variance)

  /** Proportion of variance explained by each principal component
    */
  lazy val propvar = variance / totVar

  /** Cumulative variance of the principal components
    */
  lazy val cumuvar = DenseVector(propvar.toArray.scanLeft(0.0)(_ + _) drop (1))

  /** Prints a summary of the PCA to console
    */
  def summary: Unit = {
    println("Standard deviations:")
    println(names.mkString("\t"))
    println((sdev.toArray map ("%6.3f".format(_))).mkString("\t"))
    println("Cumulative proportion of variance explained:")
    println(names.mkString("\t"))
    println((cumuvar.toArray map ("%6.3f".format(_))).mkString("\t"))
    println("Loadings:")
    ((1 to p) map ("PC%02d\t".format(_))).foreach(print)
    println("")
    (0 until p) foreach { i =>
      (0 until p) foreach { j =>
        print("%6.3f\t".format(loadings(i, j)))
      }
      println(names(i))
    }
  }

  import breeze.plot._

  /** Diagnostic plots for the PCA
    */
  def plots: Figure = {
    val fig = Figure("PCA Diagnostics")
    val p0 = fig.subplot(2, 2, 0)
    p0 += plot(linspace(0, p - 1, p), sdev)
    p0.title = "Standard deviations of the principal components"
    p0.xlabel = "Component"
    p0.ylabel = "Standard deviation"
    val p1 = fig.subplot(2, 2, 1)
    p1 += plot(linspace(1, p, p), propvar)
    p1.title = "Proportion of variance explained by each component"
    p1.xlabel = "Component"
    p1.ylabel = "Proportion of variance"
    val p2 = fig.subplot(2, 2, 2)
    p2 += plot(linspace(1, p, p), cumuvar)
    p2.title = "Cumulative variance explained"
    p2.xlabel = "Component"
    p2.ylabel = "Proportion of variance"
    val p3 = fig.subplot(2, 2, 3)
    p3 += plot(scores(::, 0), scores(::, 1), '.')
    p3.title = "Scores for first two components"
    p3.xlabel = "First principal component"
    p3.ylabel = "Second principal component"
    fig.refresh()
    fig
  }

} // case class Pca

object Pca {

  /** Constructor without a list of variable names
    */
  def apply(mat: DMD): Pca = {
    val p = mat.cols
    val names = (1 to p) map ("V%02d".format(_))
    Pca(mat, names)
  }

} // object Pca

// eof
