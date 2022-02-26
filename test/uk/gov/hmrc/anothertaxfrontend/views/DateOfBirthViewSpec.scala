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
import uk.gov.hmrc.anothertaxfrontend.controllers.DateOfBirthController
import uk.gov.hmrc.anothertaxfrontend.views.html.DateOfBirthPage

class DateOfBirthViewSpec extends ViewSpecBase {


  "DateOfBirthPage" when {
    "rendering a view with no errors" must {

      lazy val target: DateOfBirthPage = inject[DateOfBirthPage]
      lazy val form = inject[DateOfBirthController].dateOfBirthForm
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have the correct heading" in {
        elementText("h1") mustBe "What is your date of birth?"
      }

      "render a form with a POST method" in {
        elementAttributes("form") must contain("method" -> "POST")
      }

      "render a form that submits data to the correct route" in {
        elementAttributes("form") must contain("action" -> uk.gov.hmrc.anothertaxfrontend.controllers.routes.DateOfBirthController.submit.url)
      }

      "have a text input for the day" in {
        elementAttributes("""input[id="date.day"]""") must contain("type" -> "text")
      }

      "have a label associated to the day input with the correct text" in {
        elementText("""label[for="date.day"]""") mustBe "Day"
      }

      "have a text input for the month" in {
        elementAttributes("""input[id="date.month"]""") must contain("type" -> "text")
      }

      "have a label associated to the month input with the correct text" in {
        elementText("""label[for="date.month"]""") mustBe "Month"
      }

      "have a text input for the year" in {
        elementAttributes("""input[id="date.year"]""") must contain("type" -> "text")
      }

      "have a label associated to the year input with the correct text" in {
        elementText("""label[for="date.year"]""") mustBe "Year"
      }

      "have a button to submit form data" in {
        elementText("button") mustBe "Continue"
      }

    }

    "rendering a view with errors" must {

      val badData = Map(
        "date.year" -> "",
        "date.day" -> "",
        "date.month" -> ""
      )
      lazy val target: DateOfBirthPage = inject[DateOfBirthPage]
      lazy val form = inject[DateOfBirthController].dateOfBirthForm.bind(badData)
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have an error summary" in {
        elementText(".govuk-error-summary > h2.govuk-error-summary__title") mustBe "There is a problem"
      }

      "have an related to the day in the error summary" in {
        elementText(""".govuk-error-summary ul.govuk-error-summary__list li a[href="#date.day"]""") mustBe "Enter the day of your date of birth"
      }

      "have an related to the month in the error summary" in {
        elementText(""".govuk-error-summary ul.govuk-error-summary__list li a[href="#date.month"]""") mustBe "Enter the month of your date of birth"
      }

      "have an related to the year in the error summary" in {
        elementText(""".govuk-error-summary ul.govuk-error-summary__list li a[href="#date.year"]""") mustBe "Enter the year of your date of birth"
      }

      "have an error message against the date in the form" in {
        elementText("form p#date-error") mustBe "Error: Enter the day of your date of birth"
      }

    }

  }
}
