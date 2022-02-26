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
import uk.gov.hmrc.anothertaxfrontend.controllers.{FullNameController, routes}
import uk.gov.hmrc.anothertaxfrontend.views.html.FullNamePage

class FullNameViewSpec extends ViewSpecBase {

  "FullNamePage" when {
    "rendering a view with no errors" must {

      lazy val target: FullNamePage = inject[FullNamePage]
      lazy val form = inject[FullNameController].fullNameForm
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have the correct heading" in {
        elementText("h1") mustBe "What is your name?"
      }

      "render a form with a POST method" in {
        elementAttributes("form") must contain("method" -> "POST")
      }

      "render a form that submits data to the correct route" in {
        elementAttributes("form") must contain("action" -> routes.FullNameController.submit.url)
      }

      "have a text input for the first name" in {
        elementAttributes("#first") must contain("type" -> "text")
      }

      "have a label associated to the first name input with the correct text" in {
        elementText("""label[for="first"]""") mustBe "First name"
      }

      "have a text input for the middle names" in {
        elementAttributes("#middle") must contain("type" -> "text")
      }

      "have a label associated to the middle name input with the correct text" in {
        elementText("""label[for="middle"]""") mustBe "Middle names"
      }

      "have a text input for the last name" in {
        elementAttributes("#last") must contain("type" -> "text")
      }

      "have a label associated to the last name input with the correct text" in {
        elementText("""label[for="last"]""") mustBe "Last name"
      }

      "have a button to submit form data" in {
        elementText("button") mustBe "Continue"
      }

    }

    "rendering a view with errors" must {

      val badData: Map[String, String] = Map.empty
      lazy val target: FullNamePage = inject[FullNamePage]
      lazy val form = inject[FullNameController].fullNameForm.bind(badData)
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have an error summary" in {
        elementText(".govuk-error-summary > h2.govuk-error-summary__title") mustBe "There is a problem"
      }

      "have an related to the first name in the error summary" in {
        elementText(""".govuk-error-summary ul.govuk-error-summary__list li a[href="#first"]""") mustBe "Enter your first name"
      }

      "have an related to the last name in the error summary" in {
        elementText(""".govuk-error-summary ul.govuk-error-summary__list li a[href="#last"]""") mustBe "Enter your last name"
      }

      "have an error message against the first name in the form" in {
        elementText("form p#first-error") mustBe "Error: Enter your first name"
      }

      "have an error message against the last name in the form" in {
        elementText("form p#last-error") mustBe "Error: Enter your last name"
      }

    }

  }
}
