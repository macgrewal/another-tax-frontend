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


class AnnualIncomePageViewControllerSpec extends ControllerSpecBase {
  lazy val formatter = DateTimeFormatter.ofPattern("d-M-yyyy")
  lazy val date = LocalDate.parse("01-01-2000", formatter)

  lazy val sessionData = DataModel(
    firstName = Option("firstName"),
    middleName =  None,
    lastName =  Option("lastName"),
    dateOfBirth = Option(date),
    education = Option("No"),
    finishEducation = Option(date),
    employmentStatus = Option("Full time employment"),
    annualIncome = Option(BigDecimal(100))
  )

  private val controller = inject[AnnualIncomePageViewController]

  "AnnualIncomePageViewController" when {

    "calling annualIncomePage() with session data" must {

      val requestWithExistingData = fakeRequest.withSession("userData" -> Json.toJson(sessionData).toString())

      "return status code 200" in{
        val result = controller.annualIncomePage(requestWithExistingData)
        status(result) mustBe Status.OK
      }

      "return correct HTML" in{
        val result = controller.annualIncomePage(requestWithExistingData)
        contentType(result) mustBe Some("text/html")
        contentAsString(result) must include ("What is your annual income?")
      }
    }

    "calling annualIncomePage() with session data but no annualIncome" must {

      val requestWithExistingData = fakeRequest.withSession("userData" -> Json.toJson(DataModel(
        firstName = Option("firstName"),
        middleName =  None,
        lastName =  Option("lastName"),
        dateOfBirth = Option(date),
        education = Option("No"),
        finishEducation = Option(date),
        employmentStatus = Option("Full time employment"),
        annualIncome = None
      )).toString())

      "return status code 200" in{
        val result = controller.annualIncomePage(requestWithExistingData)
        status(result) mustBe Status.OK
      }

      "return correct HTML" in{
        val result = controller.annualIncomePage(requestWithExistingData)
        contentType(result) mustBe Some("text/html")
        contentAsString(result) must include ("What is your annual income?")
      }
    }


    "calling annualIncomePage() with no session data" must {

      "return status code 200" in{
        val result = controller.annualIncomePage(fakeRequest)
        status(result) mustBe Status.SEE_OTHER
      }

      "return correct HTML" in{
        val result = controller.annualIncomePage(fakeRequest)
        redirectLocation(result) mustBe Some(uk.gov.hmrc.anothertaxfrontend.controllers.routes.PageNotFoundViewController.pageNotFound.url)
      }
    }


    "calling annualIncomePagePost() with a valid request" must {

      val validRequest = fakeRequest.withFormUrlEncodedBody(
        "annualIncome" -> "10000"
      )

      "return 303 (Redirect)" in {
        val result = controller.annualIncomePagePost(validRequest)
        status(result) mustBe Status.SEE_OTHER
      }

      "redirect to the correct URL" in {
        val result = controller.annualIncomePagePost(validRequest)
        redirectLocation(result) mustBe Some(uk.gov.hmrc.anothertaxfrontend.controllers.routes.SummaryPageViewController.summaryPage.url)
      }

    }

    "calling annualIncomePagePost() with an empty input" must {

      val invalidRequest = fakeRequest.withFormUrlEncodedBody()

      "return 400 (Bad Request)" in {
        val result = controller.annualIncomePagePost(invalidRequest)
        status(result) mustBe Status.BAD_REQUEST
      }

      "return HTML" in {
        val result = controller.annualIncomePagePost(invalidRequest)
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
        contentAsString(result) must include ("This field is required")
      }

    }

    "calling annualIncomePagePost() with wrong input type" must {

      val invalidRequest = fakeRequest.withFormUrlEncodedBody(
        "annualIncome" -> "sdfsdf"
      ).withSession("userData" -> Json.toJson(sessionData).toString())

      "return 400 (Bad Request)" in {
        val result = controller.annualIncomePagePost(invalidRequest)
        status(result) mustBe Status.BAD_REQUEST
      }

      "return HTML" in {
        val result = controller.annualIncomePagePost(invalidRequest)
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
        contentAsString(result) must include ("Enter a valid number for your annual income")
      }

    }

  }
}

