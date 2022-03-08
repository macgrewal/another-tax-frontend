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

package uk.gov.hmrc.anothertaxfrontend.forms

import play.api.data.Form
import uk.gov.hmrc.anothertaxfrontend.Forms.DataForm
import uk.gov.hmrc.anothertaxfrontend.core.UnitSpec
import uk.gov.hmrc.anothertaxfrontend.models.DataModel

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DataFormSpec extends UnitSpec {

  val form: Form[DataModel] = new DataForm().form

  lazy val formatter = DateTimeFormatter.ofPattern("d-M-yyyy")
  lazy val date = LocalDate.parse("01-01-2000", formatter)
  val model = DataModel(
    Some("John"),
    Some("Middle"),
    Some("Doe"),
    Some(date),
    Some("Yes"),
    Some(date),
    Some("Unemployed"),
    Some(BigDecimal(100))
  )
  val data = Map(
    "firstName" -> model.firstName.getOrElse(fail("firstName was not provided")),
    "middleName" -> model.middleName.getOrElse(fail("middleName was not provided")),
    "lastName" -> model.lastName.getOrElse(fail("lastName was not provided")),
    "dateOfBirth" -> model.dateOfBirth.getOrElse(fail("dateOfBirth was not provided")).toString,
    "education" -> model.education.getOrElse(fail("education was not provided")),
    "finishEducation" -> model.finishEducation.getOrElse(fail("finishEducation was not provided")).toString,
    "employmentStatus" -> model.employmentStatus.getOrElse(fail("employmentStatus was not provided")),
    "annualIncome" -> model.annualIncome.getOrElse("No value provided").toString
  )

  "DataForm.form" when {

    "bound with data containing a valid input" must {

      "result in no errors" in {
        val result = form.bind(data)
        val errors = result.hasErrors
        errors mustBe false
      }

      "generate a populated form" in {
        val result = form.fillAndValidate(model)
        result.data mustBe data
      }

    }

    "bound with data containing a invalid input" must {
      lazy val formatter = DateTimeFormatter.ofPattern("d-M-yyyy")
      lazy val date = LocalDate.parse("01-01-2000", formatter)
      val model = DataModel(
        Some("John"),
        Some("Middle"),
        Some("Doe"),
        Some(date),
        Some("Yes"),
        Some(date),
        Some("Unemployed"),
        None
      )
      val data = Map(
        "firstName" -> model.firstName.getOrElse(fail("firstName was not provided")),
        "middleName" -> model.middleName.getOrElse(fail("middleName was not provided")),
        "lastName" -> model.lastName.getOrElse(fail("lastName was not provided")),
        "dateOfBirth" -> model.dateOfBirth.getOrElse(fail("dateOfBirth was not provided")).toString,
        "education" -> model.education.getOrElse(fail("education was not provided")),
        "finishEducation" -> model.finishEducation.getOrElse(fail("finishEducation was not provided")).toString,
        "employmentStatus" -> model.employmentStatus.getOrElse(fail("employmentStatus was not provided")),
        "annualIncome" -> model.annualIncome.getOrElse("No value provided").toString
      )

      "result in errors" in {
        val result = form.bind(data)
        val errors = result.hasErrors
        errors mustBe true
      }

    }

  }

}
