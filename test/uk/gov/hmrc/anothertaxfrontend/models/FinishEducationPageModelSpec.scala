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

class FinishEducationPageModelSpec extends UnitSpec {
  private val completeModel = FinishEducationPageModel(1,2,1990)
  private val completeModelJson: JsValue = Json.obj(
    "day" -> completeModel.day,
    "month" -> completeModel.month,
    "year" -> completeModel.year,
  )

  val model = FinishEducationPageModel(1,2,1990)

  val futureDateData = Map(
    "year" -> LocalDate.now().getYear.toString,
    "month" -> LocalDate.now().getMonthValue.toString,
    "day" -> (LocalDate.now().getDayOfMonth + 1).toString,
  )

  val form: Form[FinishEducationPageModel] = FinishEducationPageModel.form

  "FinishEducationPageModel.form" when {

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
          error.message mustBe "finishEducationPage.dayEmpty"
        }
      }

      "generate a populated form for the month value" in {
        result("month").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "finishEducationPage.monthEmpty"
        }
      }

      "generate a populated form for the year value" in {
        result("year").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "finishEducationPage.yearEmpty"
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
          error.message mustBe "finishEducationPage.dayNumber"
        }
      }

      "generate a populated form for the month value" in {
        result("month").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "finishEducationPage.monthNumber"
        }
      }

      "generate a populated form for the year value" in {
        result("year").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "finishEducationPage.yearNumber"
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
          error.message mustBe "finishEducationPage.dayRange"
        }
      }

      "generate a populated form for the month value" in {
        result("month").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "finishEducationPage.monthRange"
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

      "generate a populated form for the day value" in {
        result("").error.fold(fail("was expecting an error")) {error =>
          error.message mustBe "finishEducationPage.realDate"
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
          error.message mustBe "finishEducationPage.yearRange"
        }
      }

    }

    "bound with data containing a future date" must {

       lazy val result = form.bind(futureDateData)

      "result in an errors" in {
        val errors = result.hasErrors
        errors mustBe true
      }

      "generate a populated form" in {
        result("").error.fold(fail("was expecting an error")) { error =>
          error.message mustBe "finishEducationPage.pastDate"
        }
      }

    }

  }


  "FinishEducationPageModel" when {

    "serializing" must  {

      "render the complete model" in {
        Json.toJson(completeModel) mustBe  completeModelJson
      }

    }

    "deserializing" must  {

      "generate a complete model" in {
        Json.fromJson[FinishEducationPageModel](completeModelJson).getOrElse(fail("could not parse Json")) mustBe completeModel
      }

    }

  }


}
