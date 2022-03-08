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
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.anothertaxfrontend.core.UnitSpec

import java.time.LocalDate

class AddDOBPageModelSpec extends UnitSpec {
//  implicit val messages: Messages
  private val completeModel = AddDOBPageModel(1,2,1990)
  private val completeModelJson: JsValue = Json.obj(
    "day" -> completeModel.day,
    "month" -> completeModel.month,
    "year" -> completeModel.year
  )

  val model = AddDOBPageModel(1,2,1990)

  val futureDateData = Map(
    "year" -> "2222",
    "month" -> "1",
    "day" -> "1"
  )

  val form: Form[AddDOBPageModel] = AddDOBPageModel.form

  "AddDOBPageModel.form" when {

    "bound with data containing empty values " must {

      lazy val result = form.bind(Map(
        "year" -> "",
        "month" -> "",
        "day" -> ""
      ))

      "result in an errors" in {
        val errors = result.hasErrors
        errors mustBe true
      }

      "generate a populated form for the day value" in {
        result("day").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "dateOfBirthPage.dayNotEmpty"
        }
      }

      "generate a populated form for the month value" in {
        result("month").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "dateOfBirthPage.monthNotEmpty"
        }
      }

      "generate a populated form for the year value" in {
        result("year").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "dateOfBirthPage.yearNotEmpty"
        }
      }

    }


    "bound with data containing non integer values" must {

      lazy val result = form.bind(Map(
        "year" -> "someString",
        "month" -> "someString",
        "day" -> "someString"
      ))

      "result in an errors" in {
        val errors = result.hasErrors
        errors mustBe true
      }

      "generate a populated form for the day value" in {
        result("day").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "dateOfBirthPage.dayNumber"
        }
      }

      "generate a populated form for the month value" in {
        result("month").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "dateOfBirthPage.monthNumber"
        }
      }

      "generate a populated form for the year value" in {
        result("year").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "dateOfBirthPage.yearNumber"
        }
      }

    }

    "bound with data containing out of bound dates" must {

      lazy val result = form.bind(Map(
        "year" -> LocalDate.now().getYear.toString,
        "month" -> "13",
        "day" -> "33"
      ))

      "result in an errors" in {
        val errors = result.hasErrors
        errors mustBe true
      }

      "generate a populated form for the day value" in {
        result("day").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "dateOfBirthPage.dayRange"
        }
      }

      "generate a populated form for the month value" in {
        result("month").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "dateOfBirthPage.monthRange"
        }
      }

    }

    "bound with data containing an invalid date" must {

      lazy val result = form.bind(Map(
        "year" -> LocalDate.now().getYear.toString,
        "month" -> "2",
        "day" -> "31"
      ))

      "result in an errors" in {
        val errors = result.hasErrors
        errors mustBe true
      }

      "generate a populated form with the correct value" in {
        result("").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "dateOfBirthPage.realDate"
        }
      }

    }

    "bound with data containing a year before 1900" must {

      lazy val result = form.bind(Map(
        "year" -> "1700",
        "month" -> "2",
        "day" -> "31"
      ))

      "result in an errors" in {
        val errors = result.hasErrors
        errors mustBe true
      }

      "generate a populated form for the year value" in {
        result("year").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "dateOfBirthPage.yearRange"
        }
      }

    }


    "bound with data containing a future date " must {

      lazy val result = form.bind(futureDateData)

      "result in an errors" in {
        val errors = result.hasErrors
        errors mustBe true
      }

      "generate a populated form" in {
        result("").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "dateOfBirthPage.pastDate"
        }
      }

    }

  }

  "AddDOBPageModel" when {

    "serializing" must  {

      "render the complete model" in {
        Json.toJson(completeModel) mustBe  completeModelJson
      }

    }

    "deserializing" must  {

      "generate a complete model" in {
        Json.fromJson[AddDOBPageModel](completeModelJson).getOrElse(fail("could not parse Json")) mustBe completeModel
      }

    }

  }

}
