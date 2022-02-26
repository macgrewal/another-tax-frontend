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

import org.scalatest.Assertion
import play.api.data.{Form, FormError}
import uk.gov.hmrc.anothertaxfrontend.core.UnitSpec

import java.time.LocalDate

class DateFormSpec extends UnitSpec {

  val form: Form[LocalDate] = DateForm.create(
    "date.day.required",
    "date.day.invalid",
    "date.month.required",
    "date.month.invalid",
    "date.year.required",
    "date.year.invalid",
    "date.invalid"
  )

  "Binding valid data to a form" when {
    "the combined data makes a form" must {

      val date = LocalDate.of(2022, 1, 31)
      val data = Map(
        "date.day" -> "31",
        "date.month" -> "1",
        "date.year" -> "2022"
      )

      "return no errors" in {
        val result = form.bind(data)
        val errors = result.hasErrors
        errors mustBe false
      }

      "return a valid date" in {
        val result = form.bind(data)
        if (result.hasErrors) {
          fail("errors reported on a valid form")
        } else {
          result.value mustBe Some(date)
        }
      }
    }
  }

  "Binding invalid data to the day part of the form" when {

    def invalidDayTest(dayInput: String)(test: FormError => Assertion): Assertion = {

      val data = Map(
        "date.day" -> dayInput,
        "date.month" -> "1",
        "date.year" -> "2022"
      )

      val result: Form[LocalDate] = form.bind(data)
      val error: Option[FormError] = result("date.day").error

      error.fold(fail("No error generated for the day when one was expected")) { error => test(error) }
    }

    "the value is missing" must {
      "return an required value error" in invalidDayTest("") { error =>
        error.message mustBe "date.day.required"
      }
    }

    "the value non-numeric characters" must {
      "return an invalid value error" in invalidDayTest("a") { error =>
        error.message mustBe "date.day.invalid"
      }
    }

    "the value contains a negative number" must {
      "return an invalid value error" in invalidDayTest("-1") { error =>
        error.message mustBe "date.day.invalid"
      }
    }

    "the value is zero" must {
      "returns an invalid value error" in invalidDayTest("0") { error =>
        error.message mustBe "date.day.invalid"
      }
    }

  }

  "Binding invalid data to the month part of the form" when {

    def invalidMonthTest(monthInput: String)(test: FormError => Assertion): Assertion = {

      val data = Map(
        "date.day" -> "2",
        "date.month" -> monthInput,
        "date.year" -> "2022"
      )

      val result: Form[LocalDate] = form.bind(data)
      val error: Option[FormError] = result("date.month").error

      error.fold(fail("No error generated for the month when one was expected")) { error => test(error) }
    }

    "the value is missing" must {
      "return an required value error" in invalidMonthTest("") { error =>
        error.message mustBe "date.month.required"
      }
    }

    "the value non-numeric characters" must {
      "return an invalid value error" in invalidMonthTest("a") { error =>
        error.message mustBe "date.month.invalid"
      }
    }

    "the value contains a negative number" must {
      "return an invalid value error" in invalidMonthTest("-1") { error =>
        error.message mustBe "date.month.invalid"
      }
    }

    "the value is zero" must {
      "returns an invalid value error" in invalidMonthTest("0") { error =>
        error.message mustBe "date.month.invalid"
      }
    }

  }

  "Binding invalid data to the year part of the form" when {

    def invalidYearTest(yearInput: String)(test: FormError => Assertion): Assertion = {

      val data = Map(
        "date.day" -> "2",
        "date.month" -> "1",
        "date.year" -> yearInput
      )

      val result: Form[LocalDate] = form.bind(data)
      val error: Option[FormError] = result("date.year").error

      error.fold(fail("No error generated for the year when one was expected")) { error => test(error) }
    }

    "the value is missing" must {
      "return an required value error" in invalidYearTest("") { error =>
        error.message mustBe "date.year.required"
      }
    }

    "the value non-numeric characters" must {
      "return an invalid value error" in invalidYearTest("a") { error =>
        error.message mustBe "date.year.invalid"
      }
    }

    "the value contains a negative number" must {
      "return an invalid value error" in invalidYearTest("-1") { error =>
        error.message mustBe "date.year.invalid"
      }
    }

    "the value is zero" must {
      "returns an invalid value error" in invalidYearTest("0") { error =>
        error.message mustBe "date.year.invalid"
      }
    }

  }

  "Binding multiple invalid date parts to a form" when {

    def invalidDateTest(dayInput: String, monthInput: String, yearInput: String)(test: Seq[FormError] => Assertion): Assertion = {

      val data = Map(
        "date.day" -> dayInput,
        "date.month" -> monthInput,
        "date.year" -> yearInput
      )

      val result: Form[LocalDate] = form.bind(data)
      result.errors match {
        case _ :: Nil => fail("Only one error when more were expected")
        case errors => test(errors)
      }
    }

    def messageExistsForKey(key: String, message: String, errors: Seq[FormError]) = {
      errors.find(_.key == key) match {
        case Some(error) => error.message mustBe message
        case _ => fail(s"Expected required error for key '$key' does not existed")
      }
    }

    "the day, month and year are not supplied" must {

      val invalidScenario = invalidDateTest("","","")(_)

      "return three errors" in invalidScenario { errors =>
        errors.size mustBe 3
      }

      "return the required message for each date part" in invalidScenario { errors =>
        messageExistsForKey("date.year", "date.year.required", errors)
        messageExistsForKey("date.month", "date.month.required", errors)
        messageExistsForKey("date.day", "date.day.required", errors)
      }
    }

    "the day, month and year all contain invalid data" must {

      val invalidScenario = invalidDateTest("one","1.1","2 0 2 2")(_)

      "return three errors" in invalidScenario { errors =>
        errors.size mustBe 3
      }

      "return the invalid message for each date part" in invalidScenario { errors =>
        messageExistsForKey("date.year", "date.year.invalid", errors)
        messageExistsForKey("date.month", "date.month.invalid", errors)
        messageExistsForKey("date.day", "date.day.invalid", errors)
      }
    }

    "only the day and month contain bad data" must {

      val invalidScenario = invalidDateTest("","1.1","2022")(_)

      "return two errors" in invalidScenario { errors =>
        errors.size mustBe 2
      }

      "return the appropriate error message for the month and day" in invalidScenario { errors =>
        messageExistsForKey("date.month", "date.month.invalid", errors)
        messageExistsForKey("date.day", "date.day.required", errors)
      }
    }

  }

  "Binding impossible data parts that do not make a date to a form" when {

    def invalidDateTest(dayInput: String, monthInput: String, yearInput: String)(test: FormError => Assertion): Assertion = {

      val data = Map(
        "date.day" -> dayInput,
        "date.month" -> monthInput,
        "date.year" -> yearInput
      )

      val result: Form[LocalDate] = form.bind(data)
      val error: Option[FormError] = result("date").error

      error.fold(fail("No error generated for the date when one was expected")) { error => test(error) }
    }

    "the day value is impossible" must {
      "return an invalid value error" in invalidDateTest("32", "1", "2022") { error =>
        error.message mustBe "date.invalid"
      }
    }

    "the month value is impossible" must {
      "return an invalid value error" in invalidDateTest("1", "13", "2022") { error =>
        error.message mustBe "date.invalid"
      }
    }

    "the date is impossible" must {
      "returns an invalid value error" in invalidDateTest("31", "2", "2022") { error =>
        error.message mustBe "date.invalid"
      }
    }

  }

  "Building a form with a date" must {

      val date = LocalDate.of(2022, 1, 31)
      val data = Map(
        "date.day" -> "31",
        "date.month" -> "1",
        "date.year" -> "2022"
      )

      "result in no errors" in {
        val result = form.fillAndValidate(date)
        val errors = result.hasErrors
        errors mustBe false
      }

      "populate a form with the correct data parts" in {
        val result = form.fillAndValidate(date)
        if (result.hasErrors) {
          fail("errors reported on a valid form")
        } else {
          result.data mustBe data
        }
      }
    }

}
