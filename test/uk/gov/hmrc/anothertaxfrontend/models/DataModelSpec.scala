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

import play.api.libs.json._
import uk.gov.hmrc.anothertaxfrontend.core.UnitSpec

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DataModelSpec extends UnitSpec {
  lazy val formatter = DateTimeFormatter.ofPattern("d-M-yyyy")
  lazy val date = LocalDate.parse("01-01-2000", formatter)

  private val completeModel = DataModel(
    Some("John"),
    Some("Middle"),
    Some("Doe"),
    Some(date),
    Some("Yes"),
    Some(date),
    Some("Unemployed"),
    Some(BigDecimal(100))
  )

  private val completeModelJson: JsValue = Json.obj(
    "firstName" -> completeModel.firstName.getOrElse(fail("firstName not set")),
    "middleName" -> completeModel.middleName.getOrElse(fail("middleName not set")),
    "lastName" -> completeModel.lastName.getOrElse(fail("lastName not set")),
    "dateOfBirth" -> completeModel.dateOfBirth.getOrElse(fail("dateOfBirth not set")),
    "education" -> completeModel.education.getOrElse(fail("education not set")),
    "finishEducation" -> completeModel.finishEducation.getOrElse(fail("finishEducation not set")),
    "employmentStatus" -> completeModel.employmentStatus.getOrElse(fail("employmentStatus not set")),
    "annualIncome" -> completeModel.annualIncome.getOrElse(fail("annualIncome not set")),
  )

  "DataModel" when {

    "serializing" must  {

      "render the complete model when all items are present" in {
        Json.toJson(completeModel) mustBe  completeModelJson
      }

    }

    "deserializing" must  {

      "generate a complete model when all items are present" in {
        Json.fromJson[DataModel](completeModelJson).getOrElse(fail("could not parse Json")) mustBe completeModel
      }

    }

  }
}
