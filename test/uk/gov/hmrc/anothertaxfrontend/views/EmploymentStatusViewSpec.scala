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
import uk.gov.hmrc.anothertaxfrontend.controllers.EmploymentStatusController
import uk.gov.hmrc.anothertaxfrontend.views.html.EmploymentStatusPage

class EmploymentStatusViewSpec extends ViewSpecBase {


  "EmploymentStatusPage" when {
    "rendering a view with no errors" must {

      lazy val target: EmploymentStatusPage = inject[EmploymentStatusPage]
      lazy val form = inject[EmploymentStatusController].employmentStatusForm
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have the correct heading" in {
        elementText("h1") mustBe "What is your employment status?"
      }

      "render a form with a POST method" in {
        elementAttributes("form") must contain("method" -> "POST")
      }

      "render a form that submits data to the correct route" in {
        elementAttributes("form") must contain("action" -> uk.gov.hmrc.anothertaxfrontend.controllers.routes.EmploymentStatusController.submit.url)
      }

      "have a radio input for the full time employment option" in {
        elementAttributes("input#value") must contain("type" -> "radio")
      }

      "have a label associated to the full time employment option with the correct text" in {
        elementText("""label[for="value"]""") mustBe "I am in full time employment"
      }

      "have a radio input for the part time employment option" in {
        elementAttributes("input#value-2") must contain("type" -> "radio")
      }

      "have a label associated to the part time employment option with the correct text" in {
        elementText("""label[for="value-2"]""") mustBe "I am in part time employment"
      }

      "have a radio input for the unemployment option" in {
        elementAttributes("input#value-3") must contain("type" -> "radio")
      }

      "have a label associated to the unemployed option with the correct text" in {
        elementText("""label[for="value-3"]""") mustBe "I am unemployed"
      }

      "have a button to submit form data" in {
        elementText("button") mustBe "Continue"
      }

    }

    "rendering a view with errors" must {

      val badData = Map.empty[String, String]
      lazy val target: EmploymentStatusPage = inject[EmploymentStatusPage]
      lazy val form = inject[EmploymentStatusController].employmentStatusForm.bind(badData)
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have an error summary" in {
        elementText(".govuk-error-summary > h2.govuk-error-summary__title") mustBe "There is a problem"
      }

      "have an related to the options in the error summary" in {
        elementText(""".govuk-error-summary ul.govuk-error-summary__list li a[href="#value"]""") mustBe "Tell us your employment status"
      }

      "have an error message against the input in the form" in {
        elementText("form p#value-error") mustBe "Error: Tell us your employment status"
      }

    }

  }
}
