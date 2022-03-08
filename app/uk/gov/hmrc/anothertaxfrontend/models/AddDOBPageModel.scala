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

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation._
import play.api.i18n.Messages
import play.api.libs.json._

import java.time.LocalDate
import scala.util.{Success, Try}


case class AddDOBPageModel(day: Int, month: Int, year: Int)

object AddDOBPageModel {
  implicit val writes: OWrites[AddDOBPageModel] = Json.writes[AddDOBPageModel]
  implicit val reads: Reads[AddDOBPageModel] = Json.reads[AddDOBPageModel]
  implicit val messages: Messages.type = Messages

  val validDobConstraint: Constraint[AddDOBPageModel] = Constraint("validDateConstraint") { model =>
    Try {
      LocalDate.of(model.year, model.month, model.day)
    } match {
      case Success(date) if date.isAfter(LocalDate.now()) => Invalid(ValidationError("dateOfBirthPage.pastDate"))
      case Success(_) => Valid
      case _ => Invalid(ValidationError("dateOfBirthPage.realDate"))
    }
  }


  val stringMustBeANumber: String => Boolean = value => value.matches("""\d*""")


  val form: Form[AddDOBPageModel] = Form(
    mapping(
      "day" -> text
        .verifying( "dateOfBirthPage.dayNotEmpty", value => value.trim.nonEmpty)
        .verifying("dateOfBirthPage.dayNumber", stringMustBeANumber)
        .transform[Int](value => value.toInt, value => value.toString)
        .verifying("dateOfBirthPage.dayRange", value => value > 0 && value <= 31)
      ,
      "month" -> text
        .verifying( "dateOfBirthPage.monthNotEmpty", value => value.trim.nonEmpty)
        .verifying("dateOfBirthPage.monthNumber", stringMustBeANumber)
        .transform[Int](value => value.toInt, value => value.toString)
        .verifying("dateOfBirthPage.monthRange", value => value > 0 && value <= 12)
      ,
      "year" -> text
        .verifying("dateOfBirthPage.yearNotEmpty", value => value.trim.nonEmpty)
        .verifying("dateOfBirthPage.yearNumber", value => value.trim.matches("""\d*"""))
        .transform[Int](value => value.toInt, value => value.toString)
        .verifying("dateOfBirthPage.yearRange", value => value > 1900)

    )(AddDOBPageModel.apply)(AddDOBPageModel.unapply)
      .verifying(validDobConstraint)
  )

}