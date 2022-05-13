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

class ConfirmationPageViewControllerSpec extends ControllerSpecBase {

  private val controller = inject[ConfirmationPageViewController]

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


  "ConfirmationPageViewController" when{
    "calling confirmationPage() with referenceNumber session" must {

      "return status code 200" in {
        val requestWithExistingData = fakeRequest.withSession("referenceNumber" -> "SDF5343S", "userData" -> Json.toJson(sessionData).toString())
        val result = controller.confirmationPage(requestWithExistingData)
        status(result) mustBe Status.OK
      }

      "return correct HTML" in {
        val requestWithExistingData = fakeRequest.withSession("referenceNumber" -> "SDF5343S", "userData" -> Json.toJson(sessionData).toString())
        val result = controller.confirmationPage(requestWithExistingData)
        contentType(result) mustBe Some("text/html")
        contentAsString(result) must include ("Application complete")
      }

    }

    "calling confirmationPage() without summaryPage session" must {

      "return status code 300 Redirect" in {
        val result = controller.confirmationPage(fakeRequest)
        status(result) mustBe Status.SEE_OTHER
      }

      "redirect to the correct url" in {
        val result = controller.confirmationPage(fakeRequest)
        redirectLocation(result) mustBe Some(uk.gov.hmrc.anothertaxfrontend.controllers.routes.PageNotFoundViewController.pageNotFound.url)
      }

    }


    "calling confirmationPage() with user under 18" must {
      val requestWithExistingData = fakeRequest.withSession("referenceNumber" -> "SDF5343S", "userData" -> Json.toJson(DataModel(
        firstName = Option("firstName"),
        middleName =  None,
        lastName =  Option("lastName"),
        dateOfBirth = Option(LocalDate.parse(s"01-01-${LocalDate.now().getYear-10}", formatter)),
        education = Option("No"),
        finishEducation = Option(date),
        employmentStatus = Option("employed"),
        annualIncome = Option(BigDecimal(100))
      )).toString())

      "return status code 200" in {
        val result = controller.confirmationPage(requestWithExistingData)
        status(result) mustBe Status.OK
      }

      "return correct HTML" in {
        val result = controller.confirmationPage(requestWithExistingData)
        contentType(result) mustBe Some("text/html")
        contentAsString(result) must include ("Application complete")
      }

    }


    "calling confirmationPage() with userData Unemployed" must {
      val requestWithExistingData = fakeRequest.withSession("referenceNumber" -> "SDF5343S", "userData" -> Json.toJson(DataModel(
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
        val result = controller.confirmationPage(requestWithExistingData)
        status(result) mustBe Status.OK
      }

      "return correct HTML" in {
        val result = controller.confirmationPage(requestWithExistingData)
        contentType(result) mustBe Some("text/html")
        contentAsString(result) must include ("Application complete")
      }

    }

    "calling confirmationPage() with userData Part time employment" must {
      val requestWithExistingData = fakeRequest.withSession("referenceNumber" -> "SDF5343S", "userData" -> Json.toJson(DataModel(
        firstName = Option("firstName"),
        middleName =  None,
        lastName =  Option("lastName"),
        dateOfBirth = Option(date),
        education = Option("No"),
        finishEducation = Option(date),
        employmentStatus = Option("Part time employment"),
        annualIncome = Option(BigDecimal(100))
      )).toString())

      "return status code 200" in {
        val result = controller.confirmationPage(requestWithExistingData)
        status(result) mustBe Status.OK
      }

      "return correct HTML" in {
        val result = controller.confirmationPage(requestWithExistingData)
        contentType(result) mustBe Some("text/html")
        contentAsString(result) must include ("Application complete")
      }

    }

    "calling confirmationPage() with userData and education -> Yes" must {

      val requestWithExistingData = fakeRequest.withSession("referenceNumber" -> "SDF5343S", "userData" -> Json.toJson(DataModel(
        firstName = Option("firstName"),
        middleName =  None,
        lastName =  Option("lastName"),
        dateOfBirth = Option(date),
        education = Option("Yes"),
        finishEducation = Option(date),
        employmentStatus = Option("Unemployed"),
        annualIncome = Option(BigDecimal(100))
      )).toString())

      "return status code 200" in {
        val result = controller.confirmationPage(requestWithExistingData)
        status(result) mustBe Status.OK
      }

      "return correct HTML" in {
        val result = controller.confirmationPage(requestWithExistingData)
        contentType(result) mustBe Some("text/html")
        contentAsString(result) must include ("Application complete")
      }

    }

  }
}
