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

import probabilisticFittingASM.sampling.parameters.{ModelFittingParameters, PoseParameters}
import scalismo.geometry._3D
import scalismo.image.DiscreteScalarImage
import scalismo.sampling.DistributionEvaluator
import scalismo.statisticalmodel.asm.ActiveShapeModel

/**
  * Hard prior that no model point can go outside of the domain of the discrete scalar image volume.
  */
case class VolumePrior(ct: DiscreteScalarImage[_3D,Float], asm: ActiveShapeModel)
  extends DistributionEvaluator[ModelFittingParameters]
  with EvaluationCaching {

  override def computeLogValue(sample: ModelFittingParameters): Double = {
    val instance = asm.statisticalModel.instance(sample.shapeParameters.parameters)
    val poseTransform = PoseParameters.toTransform(sample.poseParameters)
    val shape = instance.transform(poseTransform)
    if (shape.pointSet.points.forall{ points =>
      ct.domain.boundingBox.isDefinedAt(points)
    })
      0.0
    else
      Double.NegativeInfinity
  }

}
