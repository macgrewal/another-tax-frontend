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

package uk.gov.hmrc.anothertaxfrontend.models

import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.anothertaxfrontend.core.UnitSpec

class FullNameSpec extends UnitSpec{

  private val completeName = FullName("John", Some("Middle"), "Smith")
  private val completeNameJson: JsValue = Json.obj(
    "first" -> completeName.first,
    "middle" -> completeName.middle.getOrElse(fail("middle name not set ona complete name")),
    "last" -> completeName.last
  )

  private val partialName = FullName("John", None, "Smith")
  private val partialNameJson: JsValue = Json.obj(
    "first" -> partialName.first,
    "last" -> partialName.last
  )

  "FullName" when {

    "serialising" must {

      "render the complete name when the middle name is present" in {
        Json.toJson(completeName) mustBe completeNameJson
      }

      "render the partial name when the middle name is not present" in {
        Json.toJson(partialName) mustBe partialNameJson
      }
    }

    "deserializing" must {

      "generate a complete name when the middle name is present" in {
        Json.fromJson[FullName](completeNameJson).getOrElse(fail("could not parse json")) mustBe completeName
      }

      "generate a partial name when the middle name is not present" in {
        Json.fromJson[FullName](partialNameJson).getOrElse(fail("could not parse json")) mustBe partialName
      }
    }

  }
}
