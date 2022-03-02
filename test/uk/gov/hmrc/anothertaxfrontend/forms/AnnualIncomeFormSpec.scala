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

package uk.gov.hmrc.anothertaxfrontend.forms

import play.api.data.Form
import uk.gov.hmrc.anothertaxfrontend.core.UnitSpec

class AnnualIncomeFormSpec extends UnitSpec {

  val form: Form[BigDecimal] = AnnualIncomeForm.create()

  "AnnualIncomeForm" when {

    "bound with no data" must {
      "result in a required error" in {
        val result = form.bind(Map.empty[String, String])
        val error = result("value").error.getOrElse(fail("expected error not generated"))
        error.message mustBe "annualIncome.required"
      }
    }

    "bound with data containing no value" must {
      "result in a required error" in {
        val result = form.bind(Map("value" -> ""))
        val error = result("value").error.getOrElse(fail("expected error not generated"))
        error.message mustBe "annualIncome.required"
      }
    }

    "bound with data containing an invalid value" must {
      "result in an invalid error" in {
        val result = form.bind(Map("value" -> "bob"))
        val error = result("value").error.getOrElse(fail("expected error not generated"))
        error.message mustBe "annualIncome.invalid"
      }
    }

    "bound with data containing zero" must {
      "result in an moreThanZero error" in {
        val result = form.bind(Map("value" -> "0"))
        val error = result("value").error.getOrElse(fail("expected error not generated"))
        error.message mustBe "annualIncome.moreThanZero"
      }
    }

    "bound with data containing a negative number" must {
      "result in an moreThanZero error" in {
        val result = form.bind(Map("value" -> "-0.1"))
        val error = result("value").error.getOrElse(fail("expected error not generated"))
        error.message mustBe "annualIncome.moreThanZero"
      }
    }

    "bound with data containing more than 2 decimal places" must {
      "result in an moreThanTwoDecimalPlaces error" in {
        val result = form.bind(Map("value" -> "1.123"))
        val error = result("value").error.getOrElse(fail("expected error not generated"))
        error.message mustBe "annualIncome.moreThanTwoDecimalPlaces"
      }
    }

    "bound with valid data" must {

      val value = BigDecimal("100.01")
      val data = Map("value" -> "100.01")

      "result in no errors" in {
        val result = form.bind(data)
        val errors = result.hasErrors
        errors mustBe false
      }

      "generate a BigDecimal" in {
        val result = form.bind(data)
        if (result.hasErrors) {
          fail("errors reported on a valid form")
        } else {
          result.value mustBe Some(value)
        }
      }
    }

    "built with a value" must {

      val value = BigDecimal("100.01")
      val data = Map("value" -> "100.01")

      "generate a populated form" in {
        val result = form.fillAndValidate(value)
        result.data mustBe data
      }
    }

  }
}
