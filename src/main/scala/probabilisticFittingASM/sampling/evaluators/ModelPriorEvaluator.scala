/*
 * Copyright University of Basel, Graphics and Vision Research Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package probabilisticFittingASM.sampling.evaluators

import breeze.linalg.{DenseVector, diag}
import probabilisticFittingASM.sampling.parameters.ModelFittingParameters
import scalismo.sampling.DistributionEvaluator
import scalismo.statisticalmodel.{MultivariateNormalDistribution, StatisticalMeshModel}

/**
  * Model prior evaluator assuming a standard Gaussian distribution for the parameters.
  */
case class ModelPriorEvaluator(model: StatisticalMeshModel)
  extends DistributionEvaluator[ModelFittingParameters] {

  val mvnormal = MultivariateNormalDistribution(
    mean = DenseVector.zeros[Double](model.rank),
    cov = diag(DenseVector.ones[Double](model.rank))
  )

  override def logValue(theta: ModelFittingParameters): Double = {
    mvnormal.logpdf(theta.shapeParameters.parameters)
  }
}