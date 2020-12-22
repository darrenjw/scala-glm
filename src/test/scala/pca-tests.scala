/*
pca-tests.scala

Test code for PCA

 */

package scalaglm

import breeze.linalg._
import breeze.numerics._

import org.scalatest._
import flatspec._
import matchers._

class PcaSpec extends AnyFlatSpec {

  "Pca" should "give the same results as Breeze" in {
    val X = DenseMatrix((1.1,1.5),(1.5,2.2),(2.0,1.5),(2.1,3.2))
    val pca = Pca(X,List("V1","V2"))
    val bPca = new PCA(X,breeze.stats.covmat(X))
    assert(norm(pca.sdev - bPca.sdev) < 0.00001)
    assert(sum(abs(abs(pca.loadings) - abs(bPca.loadings.t))) < 0.00001)
    assert(sum(abs(abs(pca.scores) - abs(bPca.scores))) < 0.00001)
  }





}
