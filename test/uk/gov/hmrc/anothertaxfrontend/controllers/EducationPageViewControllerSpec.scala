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


class EducationPageViewControllerSpec extends ControllerSpecBase {

  private val controller = inject[EducationPageViewController]
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

  "EducationPageViewController" when {

    "calling educationPage() with session data" must {
      val requestWithExistingData = fakeRequest.withSession("userData" -> Json.toJson(sessionData).toString())
      "return 200 (Ok)" in {
        val result = controller.educationPage(requestWithExistingData)
        status(result) mustBe Status.OK
      }

      "return HTML" in {
        val result = controller.educationPage(requestWithExistingData)
        contentType(result) mustBe Some("text/html")
        contentAsString(result) must include ("Are you still in education?")
      }

    }


    "calling educationPage() with no session data" must {

      "return status code 300 (Redirect)" in {
        val result = controller.educationPage(fakeRequest)
        status(result) mustBe Status.SEE_OTHER
      }

      "redirect to the correct URL" in {
        val result = controller.educationPage(fakeRequest)
        redirectLocation(result) mustBe Some(uk.gov.hmrc.anothertaxfrontend.controllers.routes.PageNotFoundViewController.pageNotFound.url)
      }

    }

    "calling educationPage() with missing education" must {
      val requestWithExistingData = fakeRequest.withSession("userData" -> Json.toJson(DataModel(
        firstName = Option("firstName"),
        middleName =  None,
        lastName =  Option("lastName"),
        dateOfBirth = Option(date),
        education = None,
        finishEducation = Option(date),
        employmentStatus = None,
        annualIncome = Option(BigDecimal(100))
      )).toString())

      "return status code 200 (Ok) " in {
        val result = controller.educationPage(requestWithExistingData)
        status(result) mustBe Status.OK
      }

      "return HTML" in {
        val result = controller.educationPage(requestWithExistingData)
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
        contentAsString(result) must include ("Are you still in education?")
      }

    }


    "calling educationPagePost() with a valid request and education is equal to 'No' " must {
      val validRequest = fakeRequest.withFormUrlEncodedBody(
        "education" -> "No"
      ).withSession("userData" -> Json.toJson(sessionData).toString())

      "return 303 (Redirect)" in {
        val result = controller.educationPagePost(validRequest)
        status(result) mustBe Status.SEE_OTHER
      }

      "redirect to the correct URL" in {
        val result = controller.educationPagePost(validRequest)
        redirectLocation(result) mustBe Some(uk.gov.hmrc.anothertaxfrontend.controllers.routes.FinishEducationPageViewController.finishEducationPage.url)
      }

    }


    "calling educationPagePost() with a valid request and education is equal to 'Yes' " must {
      val validRequest = fakeRequest.withFormUrlEncodedBody(
        "education" -> "Yes"
      ).withSession("userData" -> Json.toJson(sessionData).toString())

      "return 303 (Redirect)" in {
        val result = controller.educationPagePost(validRequest)
        status(result) mustBe Status.SEE_OTHER
      }

      "redirect to the correct URL" in {
        val result = controller.educationPagePost(validRequest)
        redirectLocation(result) mustBe Some(uk.gov.hmrc.anothertaxfrontend.controllers.routes.SummaryPageViewController.summaryPage.url)
      }

    }


    "calling educationPagePost() with a valid request and education is equal to 'Yes' with no session data" must {
      val validRequest = fakeRequest.withFormUrlEncodedBody(
        "education" -> "Yes"
      )

      "return 303 (Redirect)" in {
        val result = controller.educationPagePost(validRequest)
        status(result) mustBe Status.SEE_OTHER
      }

      "redirect to the correct URL" in {
        val result = controller.educationPagePost(validRequest)
        redirectLocation(result) mustBe Some(uk.gov.hmrc.anothertaxfrontend.controllers.routes.SummaryPageViewController.summaryPage.url)
      }

    }


    "calling educationPagePost() with an invalid request" must {

      val invalidRequest = fakeRequest.withFormUrlEncodedBody().withSession("userData" -> Json.toJson(sessionData).toString())

      "return 400 (Bad Request)" in {
        val result = controller.educationPagePost(invalidRequest)
        status(result) mustBe Status.BAD_REQUEST
      }

      "return HTML" in {
        val result = controller.educationPagePost(invalidRequest)
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
        contentAsString(result) must include ("Select one of the options")
      }

    }

  }

}
