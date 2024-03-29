pages = [{"l":"api/index.html","n":"API","t":" API","d":"api"},
{"l":"api/scalaglm.html","n":"scalaglm","t":"package scalaglm","d":"scalaglm"},
{"l":"api/scalaglm/Basis$.html","n":"Basis","t":"object Basis","d":"scalaglm/Basis$"},
{"l":"api/scalaglm/Basis$.html","n":"poly","t":"def poly(x: DenseVector[Double], degree: Int, raw: Boolean): DenseMatrix[Double]","d":"scalaglm/Basis$"},
{"l":"api/scalaglm/Basis$.html","n":"legendre","t":"def legendre(x: Double, n: Int): Double","d":"scalaglm/Basis$"},
{"l":"api/scalaglm/Basis$.html","n":"cosine","t":"def cosine(x: DenseVector[Double], n: Int): DenseMatrix[Double]","d":"scalaglm/Basis$"},
{"l":"api/scalaglm/Basis$.html","n":"cosine","t":"def cosine(x: Double, j: Int): Double","d":"scalaglm/Basis$"},
{"l":"api/scalaglm/Basis$.html","n":"bs","t":"def bs(x: DenseVector[Double], degree: Int, intercept: Boolean)(intKnots: Seq[Double], lb: Double, ub: Double): DenseMatrix[Double]","d":"scalaglm/Basis$"},
{"l":"api/scalaglm/Basis$.html","n":"bspline","t":"def bspline(x: Double, i: Int, deg: Int, knots: Vector[Double]): Double","d":"scalaglm/Basis$"},
{"l":"api/scalaglm/Glm.html","n":"Glm","t":"class Glm(y: DenseVector[Double], Xmat: DenseMatrix[Double], colNames: Seq[String], fam: GlmFamily, addIntercept: Boolean, its: Int) extends Model","d":"scalaglm/Glm"},
{"l":"api/scalaglm/Glm.html","n":"X","t":"val X: DenseMatrix[Double]","d":"scalaglm/Glm"},
{"l":"api/scalaglm/Glm.html","n":"names","t":"val names: Seq[String]","d":"scalaglm/Glm"},
{"l":"api/scalaglm/Glm.html","n":"irls","t":"val irls: (DenseVector[Double], DenseMatrix[Double], DenseMatrix[Double])","d":"scalaglm/Glm"},
{"l":"api/scalaglm/Glm.html","n":"coefficients","t":"val coefficients: DenseVector[Double]","d":"scalaglm/Glm"},
{"l":"api/scalaglm/Glm.html","n":"q","t":"val q: DenseMatrix[Double]","d":"scalaglm/Glm"},
{"l":"api/scalaglm/Glm.html","n":"r","t":"val r: DenseMatrix[Double]","d":"scalaglm/Glm"},
{"l":"api/scalaglm/Glm.html","n":"n","t":"val n: Int","d":"scalaglm/Glm"},
{"l":"api/scalaglm/Glm.html","n":"pp","t":"val pp: Int","d":"scalaglm/Glm"},
{"l":"api/scalaglm/Glm.html","n":"df","t":"val df: Int","d":"scalaglm/Glm"},
{"l":"api/scalaglm/Glm.html","n":"ri","t":"val ri: DenseMatrix[Double]","d":"scalaglm/Glm"},
{"l":"api/scalaglm/Glm.html","n":"se","t":"val se: DenseVector[Double]","d":"scalaglm/Glm"},
{"l":"api/scalaglm/Glm.html","n":"z","t":"val z: DenseVector[Double]","d":"scalaglm/Glm"},
{"l":"api/scalaglm/Glm.html","n":"p","t":"val p: DenseVector[Double]","d":"scalaglm/Glm"},
{"l":"api/scalaglm/Glm.html","n":"summary","t":"def summary: Unit","d":"scalaglm/Glm"},
{"l":"api/scalaglm/Glm.html","n":"predict","t":"def predict(newX: DenseMatrix[Double], response: Boolean): PredictGlm","d":"scalaglm/Glm"},
{"l":"api/scalaglm/Glm.html","n":"fitted","t":"val fitted: DenseVector[Double]","d":"scalaglm/Glm"},
{"l":"api/scalaglm/Glm.html","n":"plots","t":"def plots: Figure","d":"scalaglm/Glm"},
{"l":"api/scalaglm/Glm$.html","n":"Glm","t":"object Glm","d":"scalaglm/Glm$"},
{"l":"api/scalaglm/Glm$.html","n":"apply","t":"def apply(y: DenseVector[Double], Xmat: DenseMatrix[Double], fam: GlmFamily, addIntercept: Boolean, its: Int): Glm","d":"scalaglm/Glm$"},
{"l":"api/scalaglm/Glm$.html","n":"apply","t":"def apply(y: DenseVector[Double], Xmat: DenseMatrix[Double], fam: GlmFamily, its: Int): Glm","d":"scalaglm/Glm$"},
{"l":"api/scalaglm/Glm$.html","n":"apply","t":"def apply(y: DenseVector[Double], Xmat: DenseMatrix[Double], fam: GlmFamily, addIntercept: Boolean): Glm","d":"scalaglm/Glm$"},
{"l":"api/scalaglm/Glm$.html","n":"apply","t":"def apply(y: DenseVector[Double], Xmat: DenseMatrix[Double], fam: GlmFamily): Glm","d":"scalaglm/Glm$"},
{"l":"api/scalaglm/GlmFamily.html","n":"GlmFamily","t":"trait GlmFamily","d":"scalaglm/GlmFamily"},
{"l":"api/scalaglm/GlmFamily.html","n":"bp","t":"val bp: Double => Double","d":"scalaglm/GlmFamily"},
{"l":"api/scalaglm/GlmFamily.html","n":"bpp","t":"val bpp: Double => Double","d":"scalaglm/GlmFamily"},
{"l":"api/scalaglm/Irls$.html","n":"Irls","t":"object Irls","d":"scalaglm/Irls$"},
{"l":"api/scalaglm/Irls$.html","n":"IRLS","t":"def IRLS(bp: Double => Double, bpp: Double => Double, y: DenseVector[Double], X: DenseMatrix[Double], bhat0: DenseVector[Double], its: Int, tol: Double): (DenseVector[Double], DenseMatrix[Double], DenseMatrix[Double])","d":"scalaglm/Irls$"},
{"l":"api/scalaglm/Lm.html","n":"Lm","t":"class Lm(y: DenseVector[Double], Xmat: DenseMatrix[Double], colNames: Seq[String], addIntercept: Boolean) extends Model","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"X","t":"val X: DenseMatrix[Double]","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"names","t":"val names: Seq[String]","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"QR","t":"val QR: QR[DenseMatrix[Double]]","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"q","t":"val q: DenseMatrix[Double]","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"r","t":"val r: DenseMatrix[Double]","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"qty","t":"val qty: DenseVector[Double]","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"coefficients","t":"val coefficients: DenseVector[Double]","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"fitted","t":"val fitted: DenseVector[Double]","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"residuals","t":"val residuals: DenseVector[Double]","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"n","t":"val n: Int","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"pp","t":"val pp: Int","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"df","t":"val df: Int","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"rss","t":"val rss: Double","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"rse","t":"val rse: Double","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"ri","t":"val ri: DenseMatrix[Double]","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"se","t":"val se: DenseVector[Double]","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"t","t":"val t: DenseVector[Double]","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"p","t":"val p: DenseVector[Double]","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"ybar","t":"val ybar: Double","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"ymyb","t":"val ymyb: DenseVector[Double]","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"ssy","t":"val ssy: Double","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"rSquared","t":"val rSquared: Double","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"adjRs","t":"val adjRs: Double","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"k","t":"val k: Int","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"f","t":"val f: Double","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"pf","t":"val pf: Double","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"summary","t":"def summary: Unit","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"predict","t":"def predict(newX: DenseMatrix[Double]): PredictLm","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"plots","t":"def plots: Figure","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"sh","t":"val sh: DenseVector[Double]","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"h","t":"val h: Vector[Double]","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm.html","n":"studentised","t":"val studentised: DenseVector[Double]","d":"scalaglm/Lm"},
{"l":"api/scalaglm/Lm$.html","n":"Lm","t":"object Lm","d":"scalaglm/Lm$"},
{"l":"api/scalaglm/Lm$.html","n":"apply","t":"def apply(y: DenseVector[Double], Xmat: DenseMatrix[Double], addIntercept: Boolean): Lm","d":"scalaglm/Lm$"},
{"l":"api/scalaglm/Lm$.html","n":"apply","t":"def apply(y: DenseVector[Double], Xmat: DenseMatrix[Double]): Lm","d":"scalaglm/Lm$"},
{"l":"api/scalaglm/LogisticGlm$.html","n":"LogisticGlm","t":"object LogisticGlm extends GlmFamily","d":"scalaglm/LogisticGlm$"},
{"l":"api/scalaglm/LogisticGlm$.html","n":"bp","t":"val bp: Double => Double","d":"scalaglm/LogisticGlm$"},
{"l":"api/scalaglm/LogisticGlm$.html","n":"bpp","t":"val bpp: Double => Double","d":"scalaglm/LogisticGlm$"},
{"l":"api/scalaglm/Model.html","n":"Model","t":"trait Model","d":"scalaglm/Model"},
{"l":"api/scalaglm/Model.html","n":"X","t":"val X: DenseMatrix[Double]","d":"scalaglm/Model"},
{"l":"api/scalaglm/Model.html","n":"names","t":"val names: Seq[String]","d":"scalaglm/Model"},
{"l":"api/scalaglm/Model.html","n":"coefficients","t":"val coefficients: DenseVector[Double]","d":"scalaglm/Model"},
{"l":"api/scalaglm/Pca.html","n":"Pca","t":"class Pca(mat: DenseMatrix[Double], colNames: Seq[String])","d":"scalaglm/Pca"},
{"l":"api/scalaglm/Pca.html","n":"n","t":"val n: Int","d":"scalaglm/Pca"},
{"l":"api/scalaglm/Pca.html","n":"p","t":"val p: Int","d":"scalaglm/Pca"},
{"l":"api/scalaglm/Pca.html","n":"names","t":"val names: List[String]","d":"scalaglm/Pca"},
{"l":"api/scalaglm/Pca.html","n":"xBar","t":"val xBar: DenseVector[Double]","d":"scalaglm/Pca"},
{"l":"api/scalaglm/Pca.html","n":"x","t":"val x: DenseMatrix[Double]","d":"scalaglm/Pca"},
{"l":"api/scalaglm/Pca.html","n":"SVD","t":"val SVD: SVD[DenseMatrix[Double], DenseVector[Double]]","d":"scalaglm/Pca"},
{"l":"api/scalaglm/Pca.html","n":"loadings","t":"val loadings: DenseMatrix[Double]","d":"scalaglm/Pca"},
{"l":"api/scalaglm/Pca.html","n":"sdev","t":"val sdev: DenseVector[Double]","d":"scalaglm/Pca"},
{"l":"api/scalaglm/Pca.html","n":"scores","t":"val scores: DenseMatrix[Double]","d":"scalaglm/Pca"},
{"l":"api/scalaglm/Pca.html","n":"variance","t":"val variance: DenseVector[Double]","d":"scalaglm/Pca"},
{"l":"api/scalaglm/Pca.html","n":"totVar","t":"val totVar: Double","d":"scalaglm/Pca"},
{"l":"api/scalaglm/Pca.html","n":"propvar","t":"val propvar: DenseVector[Double]","d":"scalaglm/Pca"},
{"l":"api/scalaglm/Pca.html","n":"cumuvar","t":"val cumuvar: DenseVector[Double]","d":"scalaglm/Pca"},
{"l":"api/scalaglm/Pca.html","n":"summary","t":"def summary: Unit","d":"scalaglm/Pca"},
{"l":"api/scalaglm/Pca.html","n":"plots","t":"def plots: Figure","d":"scalaglm/Pca"},
{"l":"api/scalaglm/Pca$.html","n":"Pca","t":"object Pca","d":"scalaglm/Pca$"},
{"l":"api/scalaglm/Pca$.html","n":"apply","t":"def apply(mat: DenseMatrix[Double]): Pca","d":"scalaglm/Pca$"},
{"l":"api/scalaglm/PoissonGlm$.html","n":"PoissonGlm","t":"object PoissonGlm extends GlmFamily","d":"scalaglm/PoissonGlm$"},
{"l":"api/scalaglm/PoissonGlm$.html","n":"bp","t":"val bp: Double => Double","d":"scalaglm/PoissonGlm$"},
{"l":"api/scalaglm/PoissonGlm$.html","n":"bpp","t":"val bpp: Double => Double","d":"scalaglm/PoissonGlm$"},
{"l":"api/scalaglm/Predict.html","n":"Predict","t":"trait Predict","d":"scalaglm/Predict"},
{"l":"api/scalaglm/Predict.html","n":"fitted","t":"val fitted: DenseVector[Double]","d":"scalaglm/Predict"},
{"l":"api/scalaglm/PredictGlm.html","n":"PredictGlm","t":"class PredictGlm(mod: Glm, newX: DenseMatrix[Double], response: Boolean) extends Predict","d":"scalaglm/PredictGlm"},
{"l":"api/scalaglm/PredictGlm.html","n":"nX","t":"val nX: DenseMatrix[Double]","d":"scalaglm/PredictGlm"},
{"l":"api/scalaglm/PredictGlm.html","n":"lp","t":"val lp: DenseVector[Double]","d":"scalaglm/PredictGlm"},
{"l":"api/scalaglm/PredictGlm.html","n":"fitted","t":"val fitted: DenseVector[Double]","d":"scalaglm/PredictGlm"},
{"l":"api/scalaglm/PredictGlm.html","n":"rtix","t":"val rtix: DenseMatrix[Double]","d":"scalaglm/PredictGlm"},
{"l":"api/scalaglm/PredictGlm.html","n":"selp","t":"val selp: DenseVector[Double]","d":"scalaglm/PredictGlm"},
{"l":"api/scalaglm/PredictGlm.html","n":"se","t":"val se: DenseVector[Double]","d":"scalaglm/PredictGlm"},
{"l":"api/scalaglm/PredictLm.html","n":"PredictLm","t":"class PredictLm(mod: Lm, newX: DenseMatrix[Double]) extends Predict","d":"scalaglm/PredictLm"},
{"l":"api/scalaglm/PredictLm.html","n":"nX","t":"val nX: DenseMatrix[Double]","d":"scalaglm/PredictLm"},
{"l":"api/scalaglm/PredictLm.html","n":"fitted","t":"val fitted: DenseVector[Double]","d":"scalaglm/PredictLm"},
{"l":"api/scalaglm/PredictLm.html","n":"xrti","t":"val xrti: DenseMatrix[Double]","d":"scalaglm/PredictLm"},
{"l":"api/scalaglm/PredictLm.html","n":"se","t":"val se: DenseVector[Double]","d":"scalaglm/PredictLm"},
{"l":"api/scalaglm/Utils$.html","n":"Utils","t":"object Utils","d":"scalaglm/Utils$"},
{"l":"api/scalaglm/Utils$.html","n":"backSolve","t":"def backSolve(A: DenseMatrix[Double], y: DenseVector[Double]): DenseVector[Double]","d":"scalaglm/Utils$"},
{"l":"api/scalaglm/Utils$.html","n":"backSolve","t":"def backSolve(A: DenseMatrix[Double], Y: DenseMatrix[Double]): DenseMatrix[Double]","d":"scalaglm/Utils$"},
{"l":"api/scalaglm/Utils$.html","n":"forwardSolve","t":"def forwardSolve(A: DenseMatrix[Double], y: DenseVector[Double]): DenseVector[Double]","d":"scalaglm/Utils$"},
{"l":"api/scalaglm/Utils$.html","n":"bdm2aa","t":"def bdm2aa(m: DenseMatrix[Double]): Array[Array[Double]]","d":"scalaglm/Utils$"},
{"l":"api/scalaglm/Utils$.html","n":"aa2bdm","t":"def aa2bdm(a: Array[Array[Double]]): DenseMatrix[Double]","d":"scalaglm/Utils$"},
{"l":"api/scalaglm/Utils$.html","n":"tCDF","t":"def tCDF(t: Double, df: Double): Double","d":"scalaglm/Utils$"},
{"l":"api/scalaglm/Utils$.html","n":"fCDF","t":"def fCDF(x: Double, d1: Double, d2: Double): Double","d":"scalaglm/Utils$"},
{"l":"api/scalaglm/Utils$.html","n":"time","t":"def time[A](f: => A): A","d":"scalaglm/Utils$"},
{"l":"api/scalaglm/Utils$.html","n":"pairs","t":"def pairs(mat: DenseMatrix[Double], names: Seq[String]): Figure","d":"scalaglm/Utils$"},
{"l":"api/scalaglm/Utils$.html","n":"pairs","t":"def pairs(mat: DenseMatrix[Double]): Figure","d":"scalaglm/Utils$"},
{"l":"api/index.html","n":"API","t":" API","d":"api"}];