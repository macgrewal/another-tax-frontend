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
import uk.gov.hmrc.anothertaxfrontend.models.DataModel
import uk.gov.hmrc.anothertaxfrontend.views.html.SummaryPageView

import java.time.LocalDate
import java.time.format.DateTimeFormatter


class SummaryPageViewSpec extends ViewSpecBase {
  "The Summary Page View" when {

    "rendering a view" should {
      lazy val formatter = DateTimeFormatter.ofPattern("d-M-yyyy")
      lazy val target = inject[SummaryPageView]
      lazy val form =  DataModel(Option("hello"), Option("hello"), Option("hello"), Option(LocalDate.parse("02-02-1990", formatter)), Option("yes"), None, None, None)
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have the correct heading" in {
        elementText(selector = "h1") mustBe "Check your answers"
      }

      "render a form with POST method" in {
        elementAttributes(cssSelector = "form") must contain ("method" -> "POST")
      }

      "render a form that submits data to the correct route" in {
        elementAttributes(cssSelector = "form") must contain ("action" -> uk.gov.hmrc.anothertaxfrontend.controllers.routes.SummaryPageViewController.summaryPagePost.url)
      }

      "have a button to submit form data" in {
        elementText("button") mustBe "Submit tax information"
      }

    }
  }
}
