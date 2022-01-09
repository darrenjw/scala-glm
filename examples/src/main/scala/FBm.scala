/*
FBm.scala

Fractional Brownian motion via and inverse Discrete Cosine Transform (DCT)

 */

import scalaglm.Utils.*

  import breeze.linalg.*
  import breeze.numerics.*
  import breeze.stats.distributions.*
  import breeze.stats.distributions.Rand.VariableSeed.randBasis
  import java.awt.image.BufferedImage

  def dm2bi(m: DenseMatrix[Double]): BufferedImage =
    val canvas = new BufferedImage(m.cols, m.rows, BufferedImage.TYPE_INT_RGB)
    val wr = canvas.getRaster
    val mx = max(m)
    val mn = min(m)
    (0 until m.cols).foreach { x =>
      (0 until m.rows).foreach { y =>
        val shade = round(255 * (m(y, x) - mn) / (mx - mn)).toInt
        wr.setSample(x, y, 0, shade) // R
        wr.setSample(x, y, 1, shade) // G
        wr.setSample(x, y, 2, 255) // B
      }
    }
    canvas

  def showImage(m: DenseMatrix[Double]): Unit =
    import breeze.plot.*
    val fig = Figure("fBm")
    fig.width = 1200
    fig.height = 1200
    val p0 = fig.subplot(0)
    p0 += image(m)
    fig.refresh()

  @main def fBmSim() =
    val N = 1024
    val H = 0.9
    val sd = DenseMatrix.tabulate(N, N){(j, k) =>
      if (j*j + k*k < 9) 0.0 else
        math.pow(j*j + k*k, -(H + 1)/2) }
    val M = sd map (s => Gaussian(0.0, s).draw())
    val m = dct2(M, true)
    javax.imageio.ImageIO.write(dm2bi(m), "png", new java.io.File("fBm.png"))
    showImage(m)



// eof

