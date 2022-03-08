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

class AddDOBPageViewControllerSpec extends ControllerSpecBase {
  private val controller = inject[AddDOBPageViewController]

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

  "The AddDOBPageController" when {

    "calling dateOfBirth()" must {

      "return status code 200 (Ok) " in {
        val requestWithExistingData = fakeRequest.withSession("userData" -> Json.toJson(sessionData).toString())
        val result = controller.dateOfBirth(requestWithExistingData)
        status(result) mustBe Status.OK
      }


      "return HTML" in {
        val requestWithExistingData = fakeRequest.withSession("userData" -> Json.toJson(sessionData).toString())
        val result = controller.dateOfBirth(requestWithExistingData)
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
        contentAsString(result) must include ("What is your date of birth?")
      }

    }


    "calling dateOfBirth() with no session data" must {

      "return status code 300 (Redirect) " in {
        val result = controller.dateOfBirth(fakeRequest)
        status(result) mustBe Status.SEE_OTHER
      }

      "redirect to page not found" in {
        val result = controller.dateOfBirth(fakeRequest)
        redirectLocation(result) mustBe Some(uk.gov.hmrc.anothertaxfrontend.controllers.routes.PageNotFoundViewController.pageNotFound.url)
      }

    }


    "calling dateOfBirth() with missing dateOfBirth" must {
      val requestWithExistingData = fakeRequest.withSession("userData" -> Json.toJson(DataModel(
        firstName = Option("firstName"),
        middleName =  None,
        lastName =  Option("lastName"),
        dateOfBirth = None,
        education = Option("Yes"),
        finishEducation = Option(date),
        employmentStatus = Option("employed"),
        annualIncome = Option(BigDecimal(100))
      )).toString())
      "return status code 200 (Ok) " in {
        val result = controller.dateOfBirth(requestWithExistingData)
        status(result) mustBe Status.OK
      }

      "return HTML" in {
        val result = controller.dateOfBirth(requestWithExistingData)
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
        contentAsString(result) must include ("What is your date of birth?")
      }

    }


    "calling dateOfBirthPost() with a valid request" must {

      val validRequest = fakeRequest.withFormUrlEncodedBody(
        "day" -> "02",
        "month" -> "03",
        "year" -> "1990"
      ).withSession("userData" -> Json.toJson(sessionData).toString())

      "return 303 (Redirect)" in {
        val result = controller.dateOfBirthPost(validRequest)
        status(result) mustBe Status.SEE_OTHER
      }

      "redirect to the correct URL" in {
        val result = controller.dateOfBirthPost(validRequest)
        redirectLocation(result) mustBe Some(uk.gov.hmrc.anothertaxfrontend.controllers.routes.EducationPageViewController.educationPage.url)
      }

    }


    "calling dateOfBirthPost() with a valid request and no sessionData" must {

      val validRequest = fakeRequest.withFormUrlEncodedBody(
        "day" -> "02",
        "month" -> "03",
        "year" -> "1990"
      )

      "return 303 (Redirect)" in {
        val result = controller.dateOfBirthPost(validRequest)
        status(result) mustBe Status.SEE_OTHER
      }

      "redirect to the correct URL" in {
        val result = controller.dateOfBirthPost(validRequest)
        redirectLocation(result) mustBe Some(uk.gov.hmrc.anothertaxfrontend.controllers.routes.EducationPageViewController.educationPage.url)
      }

    }



    "calling dateOfBirthPost() with an invalid request" must {

      val invalidRequest = fakeRequest.withFormUrlEncodedBody(
        "day" -> "01"
      ).withSession("userData" -> Json.toJson(sessionData).toString())

      "return 400 (Bad Request)" in {
        val result = controller.dateOfBirthPost(invalidRequest)
        status(result) mustBe Status.BAD_REQUEST
      }

      "return HTML" in {
        val result = controller.dateOfBirthPost(invalidRequest)
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
        contentAsString(result) must include ("This field is required")
      }

    }

  }

}
