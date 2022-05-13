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
import play.api.data.FormBinding.Implicits.formBinding
import play.twirl.api.Html
import uk.gov.hmrc.anothertaxfrontend.models.EducationPageModel
import uk.gov.hmrc.anothertaxfrontend.views.html.EducationPageView



class EducationPageViewSpec extends ViewSpecBase {

  "The EducationPageView" when {

    "rendering a view with no errors" must {
      lazy val target: EducationPageView = inject[EducationPageView]
      lazy val form = EducationPageModel.form
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)


      "have the correct heading" in {
        elementText(selector = "h1") mustBe "Are you still in education?"
      }

      "render a form with a POST method" in {
        elementAttributes("form") must contain("method" -> "POST")
      }

      "render a form that submits data to the correct route" in {
        elementAttributes(cssSelector = "form") must contain ("action" -> uk.gov.hmrc.anothertaxfrontend.controllers.routes.EducationPageViewController.educationPagePost.url)
      }

      "have a radio button for the user to select the Yes option" in {
        elementAttributes(cssSelector = "#education") must contain ("type" -> "radio")
      }

      "have an associated label for the Yes radio button option" in {
        elementText(selector = """label[for="education"]""") mustBe "Yes"
      }

      "have a radio button for the user to select the No option" in {
        elementAttributes(cssSelector = "#education-2") must contain ("type" -> "radio")
      }

      "have an associated label for the No radio button option" in {
        elementText(selector = """label[for="education-2"]""") mustBe "No"
      }

      "have a button to submit the form data" in {
        elementText(selector = "button") mustBe "Continue"
      }

      "have a link to visit the previous page" in {
        elementAttributes(cssSelector = ".govuk-back-link") must contain ("href" -> uk.gov.hmrc.anothertaxfrontend.controllers.routes.AddDOBPageViewController.dateOfBirth.url)
      }

    }

    "rendering a view with errors" must {

      lazy val target: EducationPageView = inject[EducationPageView]
      lazy val form = EducationPageModel.form.bindFromRequest()
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have an error message against the day date input in the form" in {
        elementText("form p#education-error") mustBe "Error: Select one of the options"
      }

    }

  }
}
