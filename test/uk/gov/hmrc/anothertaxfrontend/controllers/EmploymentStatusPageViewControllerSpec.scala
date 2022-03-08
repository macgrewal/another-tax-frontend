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

class EmploymentStatusPageViewControllerSpec extends ControllerSpecBase {
  lazy val formatter = DateTimeFormatter.ofPattern("d-M-yyyy")
  lazy val date = LocalDate.parse("01-01-2000", formatter)

  lazy val sessionData = DataModel(
    firstName = Option("firstName"),
    middleName =  None,
    lastName =  Option("lastName"),
    dateOfBirth = Option(date),
    education = Option("No"),
    finishEducation = Option(date),
    employmentStatus = Option("employed"),
    annualIncome = Option(BigDecimal(100))
  )

  private val controller = inject[EmploymentStatusPageViewController]

  "EmploymentStatusPageViewController" when {


    "calling employmentStatusPage()" must {

      "return status code 200" in{
        val requestWithExistingData = fakeRequest.withSession("userData" -> Json.toJson(sessionData).toString())
        val result = controller.employmentStatusPage(requestWithExistingData)
        status(result) mustBe Status.OK
      }

      "return correct HTML" in{
        val requestWithExistingData = fakeRequest.withSession("userData" -> Json.toJson(sessionData).toString())
        val result = controller.employmentStatusPage(requestWithExistingData)
        contentType(result) mustBe Some("text/html")
        contentAsString(result) must include ("What is your employment status?")
      }

    }


    "calling employmentStatusPage() with no session data" must {

      "return status code 300 (Redirect)" in{
        val result = controller.employmentStatusPage(fakeRequest)
        status(result) mustBe Status.SEE_OTHER
      }

      "redirect to the correct url" in{
        val result = controller.employmentStatusPage(fakeRequest)
        redirectLocation(result) mustBe Some(uk.gov.hmrc.anothertaxfrontend.controllers.routes.PageNotFoundViewController.pageNotFound.url)
      }

    }

    "calling employmentStatusPage() with missing employment status" must {
      val requestWithExistingData = fakeRequest.withSession("userData" -> Json.toJson(DataModel(
        firstName = Option("firstName"),
        middleName =  None,
        lastName =  Option("lastName"),
        dateOfBirth = Option(date),
        education = Option("No"),
        finishEducation = Option(date),
        employmentStatus = None,
        annualIncome = Option(BigDecimal(100))
      )).toString())

      "return status code 200 (Ok) " in {
        val result = controller.employmentStatusPage(requestWithExistingData)
        status(result) mustBe Status.OK
      }

      "return HTML" in {
        val result = controller.employmentStatusPage(requestWithExistingData)
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
        contentAsString(result) must include ("What is your employment status?")
      }

    }


    "calling employmentStatusPagePost() with a valid request when user is 'Unemployed' " must {

      val validRequest = fakeRequest.withFormUrlEncodedBody(
        "employmentStatus" -> "Unemployed"
      )

      "return 303 (Redirect)" in {
        val result = controller.employmentStatusPagePost(validRequest)
        status(result) mustBe Status.SEE_OTHER
      }

      "redirect to the correct URL" in {
        val result = controller.employmentStatusPagePost(validRequest)
        redirectLocation(result) mustBe Some(uk.gov.hmrc.anothertaxfrontend.controllers.routes.SummaryPageViewController.summaryPage.url)
      }

    }


    "calling employmentStatusPagePost() with a valid request when user is  part time or full time employment " must {

      val validRequest = fakeRequest.withFormUrlEncodedBody(
        "employmentStatus" -> "Full time employment"
      )

      "return 303 (Redirect)" in {
        val result = controller.employmentStatusPagePost(validRequest)
        status(result) mustBe Status.SEE_OTHER
      }

      "redirect to the correct URL" in {
        val result = controller.employmentStatusPagePost(validRequest)
        redirectLocation(result) mustBe Some(uk.gov.hmrc.anothertaxfrontend.controllers.routes.AnnualIncomePageViewController.annualIncomePage.url)
      }

    }


    "calling dateOfBirthPost() with an invalid request" must {

      val invalidRequest = fakeRequest.withFormUrlEncodedBody().withSession("userData" -> Json.toJson(sessionData).toString())

      "return 400 (Bad Request)" in {
        val result = controller.employmentStatusPagePost(invalidRequest)
        status(result) mustBe Status.BAD_REQUEST
      }

      "return HTML" in {
        val result = controller.employmentStatusPagePost(invalidRequest)
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
        contentAsString(result) must include ("Select one of the options")
      }

    }


  }
}
