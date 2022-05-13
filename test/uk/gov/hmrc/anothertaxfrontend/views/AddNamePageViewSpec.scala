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
import uk.gov.hmrc.anothertaxfrontend.models.AddNamePageModel
import uk.gov.hmrc.anothertaxfrontend.views.html.AddNamePageView

class AddNamePageViewSpec extends ViewSpecBase {

  "The AddNamePageView" when {

    "rendering  a view with no errors" should {
      lazy val target: AddNamePageView = inject[AddNamePageView]
      lazy val form = AddNamePageModel.form
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have the correct heading" in {
        elementText(selector = "h1") mustBe "Enter your name"
      }

      "render a from with a POST method" in {
        elementAttributes(cssSelector = "form") must contain ("method" -> "POST")
      }

      "render a form that submits data to the correct route" in {
        elementAttributes(cssSelector = "form") must contain ("action" -> uk.gov.hmrc.anothertaxfrontend.controllers.routes.AddNamePageViewController.addNamePagePost.url)
      }

      "have a text input for the first name" in {
        elementAttributes(cssSelector = "#firstName") must contain ("type" -> "text")
      }

      "have a label associated to the first name input" in {
        elementText(selector = """label[for="firstName"]""") mustBe "First name"
      }

      "have a text input for the middle name" in {
        elementAttributes(cssSelector = "#middleName") must contain ("type" -> "text")
      }

      "have a label associated to the middle name text input" in {
        elementText(selector = """label[for="middleName"]""") mustBe "Middle name (optional)"
      }

      "have a text input fot the last name" in {
        elementAttributes(cssSelector = "#lastName") must contain ("type" -> "text")
      }

      "have a label associated to the last name text input" in  {
        elementText(selector = """label[for="lastName"]""") mustBe "Last name"
      }

      "have a button to submit the form data" in {
        elementText(selector = "button") mustBe "Continue"
      }

      "have a link to visit the previous page" in {
        elementAttributes(cssSelector = ".govuk-back-link") must contain ("href" -> uk.gov.hmrc.anothertaxfrontend.controllers.routes.HomePageViewController.homePage.url)
      }

    }


    "rendering a view with errors" should{
      lazy val target: AddNamePageView = inject[AddNamePageView]
      lazy val form = AddNamePageModel.form.bindFromRequest()
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)


      "have an error message against the first name text input" in {
        elementText(selector = "#firstName-error") mustBe "Error: This field is required"
      }

      "have an error message against the last name text input" in {
        elementText(selector = "#lastName-error") mustBe "Error: This field is required"
      }

    }

  }


}
