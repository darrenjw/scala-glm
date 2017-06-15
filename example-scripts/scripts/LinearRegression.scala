// Linear regression script

import scalaglm._
import breeze.linalg._

val url = "http://archive.ics.uci.edu/ml/machine-learning-databases/00291/airfoil_self_noise.dat"
val fileName = "self-noise.csv"

// download the file to disk if it hasn't been already
val file = new java.io.File(fileName)
if (!file.exists) {
  val s = new java.io.PrintWriter(file)
  val data = scala.io.Source.fromURL(url).getLines
  data.foreach(l => s.write(l.trim.split('\t').filter(_ != "").mkString("", ",", "\n")))
  s.close
}

// read the file from disk
val mat = csvread(new java.io.File(fileName))
println("Dim: " + mat.rows + " " + mat.cols)
Utils.pairs(mat, List("Freq", "Angle", "Chord", "Velo", "Thick", "Sound"))

// regression analysis
val y = mat(::, 5) // response is the final column
val X = mat(::, 0 to 4)
val mod = Lm(y, X, List("Freq", "Angle", "Chord", "Velo", "Thick"))
mod.summary
mod.plots

// eof

