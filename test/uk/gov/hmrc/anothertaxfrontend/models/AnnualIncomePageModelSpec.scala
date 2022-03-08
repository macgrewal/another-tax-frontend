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

class AnnualIncomePageModelSpec extends UnitSpec {
  private val completeModel = AnnualIncomePageModel(100)
  private val completeModelJson: JsValue = Json.obj("annualIncome" -> completeModel.annualIncome)

  lazy val form: Form[AnnualIncomePageModel] = AnnualIncomePageModel.form
  lazy val model = AnnualIncomePageModel(100)
  lazy val data = Map("annualIncome" -> model.annualIncome.toString)

  "AnnualIncomePageModel.form" when {

    "bound with data containing a valid input" must {

      "result in no errors" in {
        val result = form.bind(data)
        val errors = result.hasErrors
        errors mustBe false
      }

      "generate a populated form" in {
        val result = form.fillAndValidate(model)
        result.data mustBe data
      }

    }

    "bound with data containing empty input" must {

      val result = form.bind(Map("annualIncome" -> ""))

      "result in no errors" in {
        val errors = result.hasErrors
        errors mustBe true
      }

      "generate a populated form for the annual income value" in {
        result("annualIncome").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "annualIncomePage.emptyInput"
        }
      }

    }


    "bound with data containing non integer input" must {

      val result = form.bind(Map("annualIncome" -> "some string"))

      "result in no errors" in {
        val errors = result.hasErrors
        errors mustBe true
      }

      "generate a populated form for the annual income value" in {
        result("annualIncome").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "annualIncomePage.number"
        }
      }

    }

    "bound with data containing a number input with more than 2 decimal places" must {

      val result = form.bind(Map("annualIncome" -> "100.356"))

      "result in no errors" in {
        val errors = result.hasErrors
        errors mustBe true
      }

      "generate a populated form for the annual income value" in {
        result("annualIncome").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "annualIncomePage.decimal"
        }
      }

    }

  }


  "AnnualIncomePageModel" when {

    "serializing" must  {

      "render the complete model" in {
        Json.toJson(completeModel) mustBe  completeModelJson
      }

    }

    "deserializing" must  {

      "generate a complete model" in {
        Json.fromJson[AnnualIncomePageModel](completeModelJson).getOrElse(fail("could not parse Json")) mustBe completeModel
      }

    }

  }

}
