/*
 * Copyright 2022 HM Revenue & Customs
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

package uk.gov.hmrc.anothertaxfrontend.models
import play.api.data.Form
import play.api.libs.json._
import uk.gov.hmrc.anothertaxfrontend.core.UnitSpec

class EmploymentStatusPageModelSpec extends UnitSpec {
  private val completeModel = EmploymentStatusPageModel(Some("Unemployed"))
  private val completeModelJson: JsValue = Json.obj("employmentStatus" -> completeModel.employmentStatus)
  val form: Form[EmploymentStatusPageModel] =  EmploymentStatusPageModel.form
  val model = EmploymentStatusPageModel(Some("Unemployed"))
  val data = Map("employmentStatus" -> model.employmentStatus.getOrElse(""))

  "EmploymentStatusPageModel.form" when {

    "bound with data containing a valid input" must {

      "result in no errors" in {
        val result = form.bind(data)
        val errors =  result.hasErrors
        errors mustBe false
      }

      "generate a populated form" in {
        val result = form.fillAndValidate(model)
        result.data mustBe data
      }

    }

  }

  "EmploymentStatusPageModel" when {

    "serializing" must  {

      "render the complete model" in {
        Json.toJson(completeModel) mustBe  completeModelJson
      }

    }

    "deserializing" must  {

      "generate a complete model" in {
        Json.fromJson[EmploymentStatusPageModel](completeModelJson).getOrElse(fail("could not parse Json")) mustBe completeModel
      }

    }

  }

}
