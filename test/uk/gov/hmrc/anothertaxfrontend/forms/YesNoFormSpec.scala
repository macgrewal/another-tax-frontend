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

class YesNoFormSpec extends UnitSpec {

  val requiredMessage = "error.required"
  val form: Form[Boolean] = YesNoForm.create(requiredMessage)

  "Binding valid data to a form" must {

    "return no errors" in {
      val result = form.bind(Map("value" -> "no"))
      val errors = result.hasErrors
      errors mustBe false
    }

    "return a false value when the input is 'no'" in {
      val result = form.bind(Map("value" -> "no"))
      if (result.hasErrors) {
        fail("errors reported on a valid form")
      } else {
        result.value mustBe Some(false)
      }
    }

    "return a true value when the input is 'yes'" in {
      val result = form.bind(Map("value" -> "yes"))
      if (result.hasErrors) {
        fail("errors reported on a valid form")
      } else {
        result.value mustBe Some(true)
      }
    }
  }

  "Binding invalid data to a form" when {

    "no data is submitted" must {

      val result = form.bind(Map.empty[String, String])

      "return a single error" in {
        result("value").errors.size mustBe 1
      }

      "return a required error message" in {
        result("value").error.fold(fail("no error present when expected")) { error =>
          error.message mustBe requiredMessage
        }
      }
    }

    "no data for the value is submitted" must {

      val result = form.bind(Map("value" -> ""))

      "return a single error" in {
        result("value").errors.size mustBe 1
      }

      "return a required error message" in {
        result("value").error.fold(fail("no error present when expected")) { error =>
          error.message mustBe requiredMessage
        }
      }
    }

    "invalid data for the value is submitted" must {

      val result = form.bind(Map("value" -> "bob"))

      "return a single error" in {
        result("value").errors.size mustBe 1
      }

      "return a required error message" in {
        result("value").error.fold(fail("no error present when expected")) { error =>
          error.message mustBe requiredMessage
        }
      }
    }

  }

  "Building a form with valid data" when {

    "filling the for with a true value" must {
      val value = true
      val data = Map("value" -> "yes")

      "result in no errors" in {
        val result = form.fillAndValidate(value)
        val errors = result.hasErrors
        errors mustBe false
      }

      "populate a form with the correct data" in {
        val result = form.fillAndValidate(value)
        if (result.hasErrors) {
          fail("errors reported on a valid form")
        } else {
          result.data mustBe data
        }
      }
    }

    "filling the for with a false value" must {
      val value = false
      val data = Map("value" -> "no")

      "result in no errors" in {
        val result = form.fillAndValidate(value)
        val errors = result.hasErrors
        errors mustBe false
      }

      "populate a form with the correct data" in {
        val result = form.fillAndValidate(value)
        if (result.hasErrors) {
          fail("errors reported on a valid form")
        } else {
          result.data mustBe data
        }
      }
    }
  }
}
