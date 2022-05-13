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
import uk.gov.hmrc.anothertaxfrontend.models.AddDOBPageModel
import uk.gov.hmrc.anothertaxfrontend.views.html.AddDOBPageView


class AddDOBPageViewSpec extends ViewSpecBase {

  "The AddDOBPageView" when{

    "rendering a view with no errors" must{
      lazy val target: AddDOBPageView = inject[AddDOBPageView]
      lazy val form =  AddDOBPageModel.form
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have the correct heading" in {
        elementText("h1") mustBe "What is your date of birth?"
      }

      "render a form with a POST method" in {
        elementAttributes("form") must contain ("method" -> "POST")
      }

      "render a form that submits data to the correct route" in {
        elementAttributes("form") must contain ("action" -> uk.gov.hmrc.anothertaxfrontend.controllers.routes.AddDOBPageViewController.dateOfBirthPost.url)
      }

      "have a text input for the user to input days" in {
        elementAttributes("#dateOfBirth-day") must contain ("type" -> "text")
      }

      "have a label associated to the first date input with the correct text" in {
        elementText("""label[for="dateOfBirth-day"]""") mustBe "Day"
      }

      "have a text input for the user to input months" in {
        elementAttributes("#dateOfBirth-month") must contain ("type" -> "text")
      }

      "have a label associated to the second date input with the correct text" in {
        elementText("""label[for="dateOfBirth-month"]""") mustBe "Month"
      }

      "have a text input for the user to input the year" in {
        elementAttributes("#dateOfBirth-year") must contain ("type" -> "text")
      }

      "have a label associated to the last date input with the correct text" in {
        elementText("""label[for="dateOfBirth-year"]""") mustBe "Year"
      }

      "have a button to submit form data" in {
        elementText("button") mustBe "Continue"
      }

      "have a link to visit the previous page" in {
        elementAttributes(cssSelector = ".govuk-back-link") must contain ("href" -> uk.gov.hmrc.anothertaxfrontend.controllers.routes.AddNamePageViewController.addNamePage.url)
      }

    }


    "rendering a view with errors" must {

      lazy val target: AddDOBPageView = inject[AddDOBPageView]
      lazy val form = AddDOBPageModel.form.bindFromRequest()
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have an error summary" in {
        elementText(".govuk-error-summary > h2.govuk-error-summary__title") mustBe "There is a problem"
      }

      "have an related to the day date input in the error summary" in {
        elementText(""".govuk-error-summary ul.govuk-error-summary__list li a[href="#dateOfBirth-day"]""") mustBe "This field is required"
      }

      "have an related to the month date input in the error summary" in {
        elementText(""".govuk-error-summary ul.govuk-error-summary__list li a[href="#dateOfBirth-month"]""") mustBe "This field is required"
      }

      "have an related to the year date input in the error summary" in {
        elementText(""".govuk-error-summary ul.govuk-error-summary__list li a[href="#dateOfBirth-year"]""") mustBe "This field is required"
      }

      "have an error message against the day date input in the form" in {
        elementText("form p#dateOfBirth-error") mustBe "Error: This field is required"
      }

    }

  }

}
