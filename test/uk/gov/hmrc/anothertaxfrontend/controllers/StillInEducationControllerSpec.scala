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

class StillInEducationControllerSpec extends ControllerSpecBase {

  private val controller = inject[StillInEducationController]

  "StillInEducationController" when {
    "calling display()" must {

      "return 200 (Ok)" in {
        val result = controller.display(fakeRequest)
        status(result) mustBe Status.OK
      }

      "return HTML" in {
        val savedDate = false
        val requestWithExistingData = fakeRequest.withSession(
          "stillInEducation" -> Json.toJson(savedDate).toString()
        )

        val result = controller.display(requestWithExistingData)
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
      }

    }

    "calling submit() stating you're still in education" must {

      val validRequest = fakeRequest.withFormUrlEncodedBody("value" -> "yes")

      "return 303 (Redirect)" in {
        val result = controller.submit(validRequest)
        status(result) mustBe Status.SEE_OTHER
      }

      "redirect to the correct URL" in {
        val result = controller.submit(validRequest)
        redirectLocation(result) mustBe Some(routes.StillInEducationController.display.url)
      }

    }

    "calling submit() stating you're not still in education" must {

      val validRequest = fakeRequest.withFormUrlEncodedBody("value" -> "no")

      "return 303 (Redirect)" in {
        val result = controller.submit(validRequest)
        status(result) mustBe Status.SEE_OTHER
      }

      "redirect to the correct URL" in {
        val result = controller.submit(validRequest)
        redirectLocation(result) mustBe Some(routes.StillInEducationController.display.url)
      }

    }

    "calling submit() with an invalid request" must {

      val invalidRequest = fakeRequest.withFormUrlEncodedBody("value" -> "")

      "return 400 (Bad Request)" in {
        val result = controller.submit(invalidRequest)
        status(result) mustBe Status.BAD_REQUEST
      }

      "return HTML" in {
        val result = controller.submit(invalidRequest)
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
      }

    }
  }

}
