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
import uk.gov.hmrc.anothertaxfrontend.views.html.AnnualIncomePageView
import uk.gov.hmrc.anothertaxfrontend.models.AnnualIncomePageModel


class AnnualIncomePageViewSpec extends ViewSpecBase {
  "The AnnualIncomePageView" when {

    "rendering a view with no errors" must{
      lazy val target: AnnualIncomePageView = inject[AnnualIncomePageView]
      lazy val form = AnnualIncomePageModel.form
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have the correct heading" in {
        elementText(selector = "h1") mustBe "What is your annual income?"
      }

      "render a form with a POST method" in {
        elementAttributes("form") must contain ("method" -> "POST")
      }

      "render a form that submits data to the correct route" in {
        elementAttributes("form") must contain ("action" -> uk.gov.hmrc.anothertaxfrontend.controllers.routes.AnnualIncomePageViewController.annualIncomePagePost.url)
      }

      "have a text input for the user to input his income" in {
        elementAttributes("#annualIncome") must contain ("type" -> "text")
      }

      "have a prefix associated to the income text input" in {
        elementText(".govuk-input__prefix") mustBe "£"
      }

      "have a button to submit form data" in {
        elementText("button") mustBe "Continue"
      }

      "have a link to visit the previous page" in {
        elementAttributes(cssSelector = ".govuk-back-link") must contain ("href" -> uk.gov.hmrc.anothertaxfrontend.controllers.routes.EmploymentStatusPageViewController.employmentStatusPage.url)
      }

    }

    "rendering a view with errors" must {

      lazy val target: AnnualIncomePageView = inject[AnnualIncomePageView]
      lazy val form = AnnualIncomePageModel.form.bindFromRequest()
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)


      "have an error message against the empty submission of the form" in {
        elementText("form p#annualIncome-error") mustBe "Error: This field is required"
      }

    }


  }
}
