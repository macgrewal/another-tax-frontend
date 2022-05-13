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


class FinishEducationPageViewControllerSpec extends ControllerSpecBase {

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
  private val controller = inject[FinishEducationPageViewController]

  "FinishEducationPageViewController" when {

    "Calling finishEducationPage() with session data" must {

      val requestWithExistingData = fakeRequest.withSession("userData" -> Json.toJson(sessionData).toString())

      "Return Status code 200" in{
        val result = controller.finishEducationPage(requestWithExistingData)
        status(result) mustBe Status.OK
      }

      "Return Correct HTML" in{
        val result = controller.finishEducationPage(requestWithExistingData)
        contentType(result) mustBe Some("text/html")
        contentAsString(result) must include ("What year did you finish education?")
      }

    }

    "Calling finishEducationPage() with session data with finishEducation not defined" must {

      val requestWithExistingData = fakeRequest.withSession("userData" -> Json.toJson(DataModel(
        firstName = Option("firstName"),
        middleName =  None,
        lastName =  Option("lastName"),
        dateOfBirth = Option(date),
        education = Option("No"),
        finishEducation = None,
        employmentStatus = Option("employed"),
        annualIncome = Option(BigDecimal(100))
      )).toString())

      "Return Status code 200" in{
        val result = controller.finishEducationPage(requestWithExistingData)
        status(result) mustBe Status.OK
      }

      "Return Correct HTML" in{
        val result = controller.finishEducationPage(requestWithExistingData)
        contentType(result) mustBe Some("text/html")
        contentAsString(result) must include ("What year did you finish education?")
      }

    }

    "Calling finishEducationPage() with no session Data" must {

      "Return Status code 300 (Redirect)" in{
        val result = controller.finishEducationPage(fakeRequest)
        status(result) mustBe Status.SEE_OTHER
      }

      "Return Correct HTML" in{
        val result = controller.finishEducationPage(fakeRequest)
        redirectLocation(result) mustBe Some(uk.gov.hmrc.anothertaxfrontend.controllers.routes.PageNotFoundViewController.pageNotFound.url)
      }

    }


    "calling finishEducationPagePost() with a valid request" must {

      val validRequest = fakeRequest.withFormUrlEncodedBody(
        "day" -> "02",
        "month" -> "03",
        "year" -> "1990"
      ).withSession("userData" -> Json.toJson(sessionData).toString())

      "return 303 (Redirect)" in {
        val result = controller.finishEducationPagePost(validRequest)
        status(result) mustBe Status.SEE_OTHER
      }

      "redirect to the correct URL" in {
        val result = controller.finishEducationPagePost(validRequest)
        redirectLocation(result) mustBe Some(uk.gov.hmrc.anothertaxfrontend.controllers.routes.EmploymentStatusPageViewController.employmentStatusPage.url)
      }

    }

    "calling dateOfBirthPost() with an invalid request" must {

      val invalidRequest = fakeRequest.withFormUrlEncodedBody(
        "day" -> "01"
      )

      "return 400 (Bad Request)" in {
        val result = controller.finishEducationPagePost(invalidRequest)
        status(result) mustBe Status.BAD_REQUEST
      }

      "return HTML" in {
        val result = controller.finishEducationPagePost(invalidRequest)
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
        contentAsString(result) must include ("This field is required")
      }

    }


  }
}
