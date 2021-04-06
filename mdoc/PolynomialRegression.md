# Polynomial Regression

The library contains code for generating polynomial regression basis functions in the object `Basis`. Their use for flexible smoothing and interpolation is illustrated below. First some imports

```scala mdoc
import breeze.linalg._
import breeze.numerics._
import breeze.stats.distributions.Gaussian
import scalaglm._
```

Next simulate some synthetic data, and plot it.
```scala mdoc
val n = 500
val x = linspace(2.0, 5.0, n)
val yt = 0.5*x + sin(x*x)
val y = yt + DenseVector(Gaussian(0.0, 1.0).sample(n).toArray)

import breeze.plot._
val fig = Figure("Synthetic data")
val p = fig.subplot(0)
p += plot(x, y, '+', name="Data")
p += plot(x, yt, name="Truth")
```

Next add some polynomial fits, using `Basis.poly` to generate the necessary covariate matrix.

```scala mdoc
(1 to 17 by 4).foreach(i => {
	val lm = Lm(y, Basis.poly(x, i))
	p += plot(x, lm.fitted, name="P"+i)
})
p.legend = true
p.title = "Polynomial fits"
```

That's it!


