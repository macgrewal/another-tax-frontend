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

class OptionsFormSpec extends UnitSpec {

  val requiredMessage = "error.required"
  val options: Seq[String] = Seq("option1", "option2", "option3")
  val form: Form[String] = OptionsForm.create(requiredMessage, options)

  options.foreach { option =>
    s"Binding known data '$option" must {

      val result = form.bind(Map("value" -> option))

      "return no errors" in {
        result.hasErrors mustBe false
      }

      s"return '$option' as the value" in {
        result.value.fold(fail("errors reported on a valid form")) { value =>
          value mustBe option
        }
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

      val result = form.bind(Map("value" -> "unknown"))

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

    "filling the form with a known value" must {
      val value = options.head
      val data = Map("value" -> options.head)

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
