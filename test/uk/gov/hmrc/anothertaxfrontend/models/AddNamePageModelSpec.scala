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

class AddNamePageModelSpec extends UnitSpec {
  private val completeModel = AddNamePageModel("John", "middle", "Doe")
  private val completeModelJson: JsValue = Json.obj(
    "firstName" -> completeModel.firstName,
    "middleName" -> completeModel.middleName,
    "lastName" -> completeModel.lastName
  )

  val form: Form[AddNamePageModel] =  AddNamePageModel.form


  "AddNamePageModel.form" when {

    "bound with data containing empty input" must {
      val result = form.bind(Map(
        "firstName" -> "",
        "middleName" -> "",
        "lastName" -> ""
      ))

      "result in errors" in {
        val errors =  result.hasErrors
        errors mustBe true
      }

      "generate a populated form for the first name value" in {
        result("firstName").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "Enter your first name"
        }
      }

      "generate a populated form for the last name value" in {
        result("lastName").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "Enter your last name"
        }
      }

    }

    "bound with data containing number" must {
      val result = form.bind(Map(
        "firstName" -> "89798",
        "middleName" -> "9879",
        "lastName" -> "09809"
      ))

      "result in errors" in {
        val errors =  result.hasErrors
        errors mustBe true
      }

      "generate a populated form for the first name value" in {
        result("firstName").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "First name cannot contain numbers or special characters"
        }
      }

      "generate a populated form for the middle name value" in {
        result("middleName").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "Middle name cannot contain numbers or special characters"
        }
      }

      "generate a populated form for the last name value" in {
        result("lastName").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "Last name cannot contain numbers or special characters"
        }
      }

    }

    "bound with data containing special characters" must {
      val result = form.bind(Map(
        "firstName" -> "@@@@",
        "middleName" -> "\\\\",
        "lastName" -> "(((()"
      ))

      "result in errors" in {
        val errors =  result.hasErrors
        errors mustBe true
      }

      "generate a populated form for the first name value" in {
        result("firstName").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "First name cannot contain numbers or special characters"
        }
      }

      "generate a populated form for the middle name value" in {
        result("middleName").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "Middle name cannot contain numbers or special characters"
        }
      }

      "generate a populated form for the last name value" in {
        result("lastName").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "Last name cannot contain numbers or special characters"
        }
      }

    }

    "bound with data containing a valid input" must {
      val model = AddNamePageModel("John", "middle", "Doe")
      val data = Map(
        "firstName" -> completeModel.firstName,
        "middleName" -> completeModel.middleName,
        "lastName" -> completeModel.lastName
      )
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

  "AddNamePageModel" when {

    "serializing" must  {

      "render the complete model" in {
        Json.toJson(completeModel) mustBe  completeModelJson
      }

    }

    "deserializing" must  {

      "generate a complete model" in {
        Json.fromJson[AddNamePageModel](completeModelJson).getOrElse(fail("could not parse Json")) mustBe completeModel
      }

    }

  }

}
