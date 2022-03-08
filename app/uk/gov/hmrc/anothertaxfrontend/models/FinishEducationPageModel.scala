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
import play.api.data.Forms._
import play.api.data.Form
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

import java.time.LocalDate
import scala.util._

case class FinishEducationPageModel(day: Int, month: Int, year: Int)

object FinishEducationPageModel {
  implicit val writes: OWrites[FinishEducationPageModel] = Json.writes[FinishEducationPageModel]
  implicit val reads: Reads[FinishEducationPageModel] = Json.reads[FinishEducationPageModel]

  val validDateConstraint: Constraint[FinishEducationPageModel] = Constraint("validDateConstraint") { model =>
    Try {
      LocalDate.of(model.year, model.month, model.day)
    } match {
      case Success(date) if date.isAfter(LocalDate.now()) => Invalid(ValidationError("finishEducationPage.pastDate"))
      case Success(_) => Valid
      case _ => Invalid(ValidationError("finishEducationPage.realDate"))
    }
  }

  val form: Form[FinishEducationPageModel] = Form(
    mapping(
      "day" -> text
        .verifying("finishEducationPage.dayEmpty", value => value.trim.nonEmpty)
        .verifying("finishEducationPage.dayNumber", value => value.trim.matches("""\d*"""))
        .transform[Int](value => value.trim.toInt, value => value.toString)
        .verifying("finishEducationPage.dayRange", value => value > 0  && value <= 31)
      ,
      "month" -> text
        .verifying("finishEducationPage.monthEmpty", value => value.trim.nonEmpty)
        .verifying("finishEducationPage.monthNumber", value => value.trim.matches("""\d*"""))
        .transform[Int](value => value.trim.toInt, value => value.toString)
        .verifying("finishEducationPage.monthRange", value => value > 0 && value <= 12)
      ,
      "year" -> text
        .verifying("finishEducationPage.yearEmpty", value => value.trim.nonEmpty)
        .verifying("finishEducationPage.yearNumber", value => value.trim.matches("""\d*"""))
        .transform[Int](value => value.trim.toInt, value => value.toString)
        .verifying("finishEducationPage.yearRange", value => value > 1900)
    )
    (FinishEducationPageModel.apply)(FinishEducationPageModel.unapply).verifying(validDateConstraint)
  )
}
