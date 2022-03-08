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

package uk.gov.hmrc.anothertaxfrontend.controllers
import play.api.http.Status
import play.api.libs.json.Json
import play.api.test.Helpers._
import uk.gov.hmrc.anothertaxfrontend.models.DataModel

import java.time.LocalDate
import java.time.format.DateTimeFormatter


class SummaryPageViewControllerSpec extends ControllerSpecBase {
  lazy val formatter = DateTimeFormatter.ofPattern("d-M-yyyy")
  lazy val date = LocalDate.parse("01-01-2000", formatter)

  lazy val sessionData = DataModel(
    firstName = Option("firstName"),
    middleName =  None,
    lastName =  Option("lastName"),
    dateOfBirth = Option(date),
    education = Option("Yes"),
    finishEducation = Option(date),
    employmentStatus = Option("employed"),
    annualIncome = Option(BigDecimal(100))
  )

  private val controller = inject[SummaryPageViewController]

  "SummaryPageViewController" when {

    "calling summaryPage() with a valid session data" must {

      "return status code 200" in {
        val requestWithExistingData = fakeRequest.withSession("userData" -> Json.toJson(sessionData).toString())
        val result = controller.summaryPage(requestWithExistingData)
        status(result) mustBe Status.OK
      }

      "return correct HTML" in {
        val requestWithExistingData = fakeRequest.withSession("userData" -> Json.toJson(sessionData).toString())
        val result = controller.summaryPage(requestWithExistingData)
        contentType(result) mustBe Some("text/html")
        contentAsString(result) must include ("Check your answers")
      }

    }

    "calling summaryPage() with a valid session data with Unemployment" must {
      val requestWithExistingData = fakeRequest.withSession("userData" -> Json.toJson(DataModel(
        firstName = Option("firstName"),
        middleName =  None,
        lastName =  Option("lastName"),
        dateOfBirth = Option(date),
        education = Option("No"),
        finishEducation = Option(date),
        employmentStatus = Option("Unemployed"),
        annualIncome = Option(BigDecimal(100))
      )).toString())
      "return status code 200" in {
        val result = controller.summaryPage(requestWithExistingData)
        status(result) mustBe Status.OK
      }

      "return correct HTML" in {
        val result = controller.summaryPage(requestWithExistingData)
        contentType(result) mustBe Some("text/html")
        contentAsString(result) must include ("Check your answers")
      }

    }

    "calling summaryPage() with no session data" must {

      "return status code 300 Redirect" in {
        val result = controller.summaryPage(fakeRequest)
        status(result) mustBe Status.SEE_OTHER
      }

      "return correct HTML" in {
        val result = controller.summaryPage(fakeRequest)
        redirectLocation(result) mustBe Some(uk.gov.hmrc.anothertaxfrontend.controllers.routes.PageNotFoundViewController.pageNotFound.url)
      }

    }


    "calling summaryPagePost()" must {

      "return 303 (Redirect)" in {
        val result = controller.summaryPagePost(fakeRequest)
        status(result) mustBe Status.SEE_OTHER
      }

      "redirect to the correct URL" in {
        val result = controller.summaryPagePost(fakeRequest)
        redirectLocation(result) mustBe Some(uk.gov.hmrc.anothertaxfrontend.controllers.routes.ConfirmationPageViewController.confirmationPage.url)
      }

    }

  }
}
