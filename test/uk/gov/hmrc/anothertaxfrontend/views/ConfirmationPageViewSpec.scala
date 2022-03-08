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
import uk.gov.hmrc.anothertaxfrontend.views.html.ConfirmationPageView

class ConfirmationPageViewSpec extends ViewSpecBase {
  "The Confirmation Page View" when {
    "rendering a view" should {
      val target = inject[ConfirmationPageView]
      val result: Html = target("you owe x in tax", Some("HJH32HB2"))
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have the correct heading" in {
        elementText(selector = "h1") mustBe "Application complete"
      }

      "have a button to submit form data" in {
        elementText("a.govuk-button") mustBe "Return home"
      }

      "have a link to visit the previous page" in {
        elementAttributes(cssSelector = ".govuk-button") must contain ("href" -> uk.gov.hmrc.anothertaxfrontend.controllers.routes.HomePageViewController.homePage.url)
      }

    }
  }
}
