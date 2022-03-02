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

package uk.gov.hmrc.anothertaxfrontend.views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.twirl.api.Html
import uk.gov.hmrc.anothertaxfrontend.controllers.{AnnualIncomeController, routes}
import uk.gov.hmrc.anothertaxfrontend.views.html.AnnualncomePage

class AnnualIncomeViewSpec extends ViewSpecBase {

  "AnnualIncomePage" when {
    "rendering a view with no errors" must {

      val data: Map[String, String] = Map("value" -> "1.1")
      lazy val target: AnnualncomePage = inject[AnnualncomePage]
      lazy val form = inject[AnnualIncomeController].annualIncomeForm.bind(data)
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have the correct heading" in {
        elementText("h1") mustBe "What is your annual income?"
      }

      "render a form with a POST method" in {
        elementAttributes("form") must contain("method" -> "POST")
      }

      "render a form that submits data to the correct route" in {
        elementAttributes("form") must contain("action" -> routes.AnnualIncomeController.submit.url)
      }

      "have a text input for the income amount" in {
        elementAttributes("#value") must contain("type" -> "text")
      }

      "have a text input pre-populated with a formatted value" in {
        elementAttributes("#value") must contain("value" -> "1.10")
      }

      "have a button to submit form data" in {
        elementText("button") mustBe "Continue"
      }

    }

    "rendering a view with a required error" must {

      val badData: Map[String, String] = Map.empty
      lazy val target: AnnualncomePage = inject[AnnualncomePage]
      lazy val form = inject[AnnualIncomeController].annualIncomeForm.bind(badData)
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have an error summary" in {
        elementText(".govuk-error-summary > h2.govuk-error-summary__title") mustBe "There is a problem"
      }

      "have an related to the annual income in the error summary" in {
        elementText(""".govuk-error-summary ul.govuk-error-summary__list li a[href="#value"]""") mustBe "Enter your annual income"
      }

      "have an error message against the annual income in the form" in {
        elementText("form p#value-error") mustBe "Error: Enter your annual income"
      }

    }

    "rendering a view with an invalid error" must {

      val badData: Map[String, String] = Map("value" -> "bob")
      lazy val target: AnnualncomePage = inject[AnnualncomePage]
      lazy val form = inject[AnnualIncomeController].annualIncomeForm.bind(badData)
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have an error summary" in {
        elementText(".govuk-error-summary > h2.govuk-error-summary__title") mustBe "There is a problem"
      }

      "have an related to the annual income in the error summary" in {
        elementText(""".govuk-error-summary ul.govuk-error-summary__list li a[href="#value"]""") mustBe "Enter a valid income amount"
      }

      "have an error message against the annual income in the form" in {
        elementText("form p#value-error") mustBe "Error: Enter a valid income amount"
      }

    }

    "rendering a view with a moreThanZero error" must {

      val badData: Map[String, String] = Map("value" -> "-1")
      lazy val target: AnnualncomePage = inject[AnnualncomePage]
      lazy val form = inject[AnnualIncomeController].annualIncomeForm.bind(badData)
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have an error summary" in {
        elementText(".govuk-error-summary > h2.govuk-error-summary__title") mustBe "There is a problem"
      }

      "have an related to the annual income in the error summary" in {
        elementText(""".govuk-error-summary ul.govuk-error-summary__list li a[href="#value"]""") mustBe "Your annual income must be more than £0"
      }

      "have an error message against the annual income in the form" in {
        elementText("form p#value-error") mustBe "Error: Your annual income must be more than £0"
      }

    }

    "rendering a view with a moreThanTwoDecimalPlaces error" must {

      val badData: Map[String, String] = Map("value" -> "1.123")
      lazy val target: AnnualncomePage = inject[AnnualncomePage]
      lazy val form = inject[AnnualIncomeController].annualIncomeForm.bind(badData)
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have an error summary" in {
        elementText(".govuk-error-summary > h2.govuk-error-summary__title") mustBe "There is a problem"
      }

      "have an related to the annual income in the error summary" in {
        elementText(""".govuk-error-summary ul.govuk-error-summary__list li a[href="#value"]""") mustBe "Enter an amount in pounds and pence"
      }

      "have an error message against the annual income in the form" in {
        elementText("form p#value-error") mustBe "Error: Enter an amount in pounds and pence"
      }

    }

  }
}
