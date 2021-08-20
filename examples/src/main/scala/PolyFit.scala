/*
PolyFit.scala

 */


import breeze.linalg._
import breeze.numerics._
import breeze.stats.distributions.Gaussian
import scalaglm._

import breeze.stats.distributions.Rand.VariableSeed.randBasis

object PolyFit {

  def main(args: Array[String]): Unit = {

    // simulate some synthetic data
    val n = 500
    val x = linspace(2.0, 5.0, n)
    val yt = 0.5*x + sin(x*x)
    val y = yt + DenseVector(Gaussian(0.0, 1.0).sample(n).toArray)

    // scatter plot
    import breeze.plot._
    val fig = Figure("Synthetic data")
    val p = fig.subplot(0)
    p += plot(x, y, '+', name="Data")
    p += plot(x, yt, name="Truth")

    // some polynomial fits
    (1 to 17 by 4).foreach(i => {
      val lm = Lm(y, Basis.poly(x, i))
      p += plot(x, lm.fitted, name="P"+i)
    })
    p.legend = true
    p.title = "Polynomial fits"

  } // main

}

// eof

