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
import uk.gov.hmrc.anothertaxfrontend.views.html.FinishEducationPageView
import uk.gov.hmrc.anothertaxfrontend.models.FinishEducationPageModel



class FinishEducationPageViewSpec extends ViewSpecBase {
  "The FinishEducationPageView" when {

    "rendering a view with no errors" must {
      lazy val target: FinishEducationPageView = inject[FinishEducationPageView]
      lazy val form =  FinishEducationPageModel.form
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "render the correct heading" in {
        elementText(selector = "h1") mustBe "What year did you finish education?"
      }

      "render a form with a POST method" in {
        elementAttributes("form") must contain ("method" -> "POST")
      }

      "render a form that submits data to the correct route" in {
        elementAttributes("form") must contain ("action" -> uk.gov.hmrc.anothertaxfrontend.controllers.routes.FinishEducationPageViewController.finishEducationPagePost.url)
      }

      "have a text input for the user to input days" in {
        elementAttributes("#finishEducation-day") must contain ("type" -> "text")
      }

      "have a label associated to the first date input with the correct text" in {
        elementText("""label[for="finishEducation-day"]""") mustBe "Day"
      }

      "have a text input for the user to input months" in {
        elementAttributes("#finishEducation-month") must contain ("type" -> "text")
      }

      "have a label associated to the second date input with the correct text" in {
        elementText("""label[for="finishEducation-month"]""") mustBe "Month"
      }

      "have a text input for the user to input the year" in {
        elementAttributes("#finishEducation-year") must contain ("type" -> "text")
      }

      "have a label associated to the last date input with the correct text" in {
        elementText("""label[for="finishEducation-year"]""") mustBe "Year"
      }

      "have a button to submit form data" in {
        elementText("button") mustBe "Continue"
      }

      "have a link to visit the previous page" in {
        elementAttributes(cssSelector = ".govuk-back-link") must contain ("href" -> uk.gov.hmrc.anothertaxfrontend.controllers.routes.EducationPageViewController.educationPage.url)
      }

    }

    "rendering a view with errors" must {

      lazy val target: FinishEducationPageView = inject[FinishEducationPageView]
      lazy val form = FinishEducationPageModel.form.bindFromRequest()
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)


      "display an error message if the form is empty" in {
        elementText("form p#finishEducation-error") mustBe "Error: This field is required"
      }

    }

//    "rendering a view with errors for submitting invalid data" must {
//
//      lazy val target: FinishEducationPageView = inject[FinishEducationPageView]
//      lazy val form = FinishEducationPageModel.form.fillAndValidate(FinishEducationPageModel("a", "b", "c"))bindFromRequest()
//      lazy val result: Html = target(form)
//      lazy implicit val document: Document = Jsoup.parse(result.body)
//
//      "display an error message if the form contains invalid data" in {
//        elementText("form p#finishEducation-error") mustBe "Error: Numeric value expected"
//      }
//
//    }


  }
}
