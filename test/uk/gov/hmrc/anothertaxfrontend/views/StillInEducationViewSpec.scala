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
import uk.gov.hmrc.anothertaxfrontend.controllers.StillInEducationController
import uk.gov.hmrc.anothertaxfrontend.views.html.StillInEducationPage

class StillInEducationViewSpec extends ViewSpecBase {


  "StillInEducationPage" when {
    "rendering a view with no errors" must {

      lazy val target: StillInEducationPage = inject[StillInEducationPage]
      lazy val form = inject[StillInEducationController].yesNoForm
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have the correct heading" in {
        elementText("h1") mustBe "Are you still in education?"
      }

      "render a form with a POST method" in {
        elementAttributes("form") must contain("method" -> "POST")
      }

      "render a form that submits data to the correct route" in {
        elementAttributes("form") must contain("action" -> uk.gov.hmrc.anothertaxfrontend.controllers.routes.StillInEducationController.submit.url)
      }

      "have a text input for the yes option" in {
        elementAttributes("input#value") must contain("type" -> "radio")
      }

      "have a label associated to the yes option with the correct text" in {
        elementText("""label[for="value"]""") mustBe "Yes"
      }

      "have a text input for the no option" in {
        elementAttributes("input#value-2") must contain("type" -> "radio")
      }

      "have a label associated to the no option with the correct text" in {
        elementText("""label[for="value-2"]""") mustBe "No"
      }

      "have a button to submit form data" in {
        elementText("button") mustBe "Continue"
      }

    }

    "rendering a view with errors" must {

      val badData = Map.empty[String, String]
      lazy val target: StillInEducationPage = inject[StillInEducationPage]
      lazy val form = inject[StillInEducationController].yesNoForm.bind(badData)
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have an error summary" in {
        elementText(".govuk-error-summary > h2.govuk-error-summary__title") mustBe "There is a problem"
      }

      "have an related to the options in the error summary" in {
        elementText(""".govuk-error-summary ul.govuk-error-summary__list li a[href="#value"]""") mustBe "Tell us if you are still in education"
      }

      "have an error message against the input in the form" in {
        elementText("form p#value-error") mustBe "Error: Tell us if you are still in education"
      }

    }

  }
}