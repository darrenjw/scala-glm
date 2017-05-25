/*
pca.scala

PCA for the dataset:

http://archive.ics.uci.edu/ml/datasets/Iris

from the Machine learning repository: 

http://archive.ics.uci.edu/ml/datasets.html

*/

package scalaglm

import breeze.linalg._
import breeze.stats._


/**
  * Principal components analysis
  * 
  * Computed using SVD of the centred data matrix rather than from the
  * spectral decomposition of the covariance matrix. eg. More like the
  * R function "prcomp" than the R function "princomp".
  * 
  * @param mat Data matrix with rows corresponding to observations and 
  * columns corresponding to variables
  * 
  * @return An object of type Pca with methods such as .loadings, .scores, and .sdev
  * 
  * NOTE: .loadings are transposed relative to the PCA function in Breeze
  */
case class Pca(mat: DenseMatrix[Double], colNames: Seq[String]) {
  require(mat.cols == colNames.length)
  val n = mat.rows
  val p = mat.cols
  val names = colNames.toList
  val xBar = mean(mat(::, *)).t
  val x = mat(*, ::) - xBar
  val SVD = svd.reduced(x)
  val loadings = SVD.Vt.t
  val sdev = SVD.S / math.sqrt(x.rows - 1)
  lazy val scores = x * loadings
  lazy val variance = sdev map (x => x*x)
  lazy val totVar = sum(variance)
  lazy val propvar = variance / totVar
  lazy val cumuvar = DenseVector(propvar.toArray.scanLeft(0.0)(_+_) drop (1))
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
        print("%6.3f\t".format(loadings(i,j)))
        }
        println(names(i))
    }
  }
}

object Pca {

  def apply(mat: DenseMatrix[Double]): Pca = {
    val p = mat.cols
    val names = (1 to p) map ("V%02d".format(_))
    Pca(mat,names)
  }

}

object PcaUtils {


  // Example main runner method
  def main(args: Array[String]): Unit = {

    val url = "http://archive.ics.uci.edu/ml/machine-learning-databases/iris/iris.data"
    val fileName = "iris.csv"
    val imap = Map(
      "Iris-setosa" -> 0,
      "Iris-versicolor" -> 1,
      "Iris-virginica" -> 2)

    // download the file to disk if it hasn't been already
    val file = new java.io.File(fileName)
    if (!file.exists) {
      val s = new java.io.PrintWriter(file)
      val data = scala.io.Source.fromURL(url).getLines
      data.foreach(l => s.write(l.trim.split(',').
        map(x => imap.getOrElse(x, x)).mkString("", ",", "\n")))
      s.close
    }

    // read the file from disk
    val mat = csvread(new java.io.File(fileName))
    println("Mat Dim: " + mat.rows + " " + mat.cols)
    val x = mat(::, 0 to 3)
    println("X Dim: " + x.rows + " " + x.cols)
    val clas = mat(::, 4).toDenseVector

    println("PCA with built-in Breeze version (like R princomp):")
    val pca = new PCA(x, covmat(x))
    println("Loadings:")
    println(pca.loadings)
    println("Stdev:")
    println(pca.sdev)
    println(pca.scores(0 to 5, ::))

    println("Now my version (like R prcomp):")
    val myPca = Pca(x, List("S-L","S-W","P-L","P-W"))
    println(myPca.loadings) // loadings transposed
    println(myPca.sdev)
    myPca.summary
    println("Scores:")
    println(myPca.scores(0 to 5, ::))



    // scatter plot first 2 principal components
    import breeze.plot._
    val fig = Figure("PCA")
    val p = fig.subplot(0)
    val ind0 = (0 until x.rows) filter (i => clas(i) == 0)
    p += plot(myPca.scores(ind0, 0).toDenseVector,
      myPca.scores(ind0, 1).toDenseVector, '.', colorcode = "blue")
    val ind1 = (0 until x.rows) filter (i => clas(i) == 1)
    p += plot(myPca.scores(ind1, 0).toDenseVector,
      myPca.scores(ind1, 1).toDenseVector, '.', colorcode = "red")
    val ind2 = (0 until x.rows) filter (i => clas(i) == 2)
    p += plot(myPca.scores(ind2, 0).toDenseVector,
      myPca.scores(ind2, 1).toDenseVector, '.', colorcode = "green")

    // Test without variable names
    Pca(x).summary


  }

}

// eof

