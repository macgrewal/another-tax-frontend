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
import play.api.libs.json._


case class AnnualIncomePageModel (annualIncome: BigDecimal)

object AnnualIncomePageModel {
  implicit val writes: OWrites[AnnualIncomePageModel] = Json.writes[AnnualIncomePageModel]
  implicit val reads: Reads[AnnualIncomePageModel] = Json.reads[AnnualIncomePageModel]

  val form: Form[AnnualIncomePageModel] = Form(
    mapping(
      "annualIncome" -> text
        .verifying("annualIncomePage.emptyInput", value => value.trim.nonEmpty)
        .verifying("annualIncomePage.number", value => value.trim.matches("""\d*\.?\d*"""))
        .verifying("annualIncomePage.decimal", value => value.trim.matches("""\d*\.?\d{0,2}"""))
        .transform[BigDecimal](value => value.toDouble, value => value.toString)
    )
    (AnnualIncomePageModel.apply)
    (AnnualIncomePageModel.unapply)
  )
}