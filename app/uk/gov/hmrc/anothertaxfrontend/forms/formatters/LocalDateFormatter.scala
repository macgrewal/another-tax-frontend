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

package uk.gov.hmrc.anothertaxfrontend.forms.formatters

import play.api.data.FormError
import play.api.data.format.Formatter

import java.time.LocalDate
import scala.util.{Success, Try}

object LocalDateFormatter {
  def formatter(dayRequiredMessageKey: String,
                dayInvalidMessageKey: String,
                monthRequiredMessageKey: String,
                monthInvalidMessageKey: String,
                yearRequiredMessageKey: String,
                yearInvalidMessageKey: String,
                dateInvalidMessageKey: String
               ): Formatter[LocalDate] = new Formatter[LocalDate] {

    private def validateDatePart(prefix: String,
                                 datePart: String,
                                 value: String,
                                 requiredMessageKey: String,
                                 invalidMessageKey: String
                                ): Seq[(String, String)] = {

      val onlyNumbersPattern = "^\\d*$"

      val error = value.trim match {
        case value if value.isEmpty => requiredMessageKey
        case value if !value.matches(onlyNumbersPattern) => invalidMessageKey
        case value if value.toInt < 1 => invalidMessageKey
        case _ => ""
      }

      if (error.isEmpty) Seq.empty else Seq(s"$prefix.$datePart" -> error)
    }

    private def validateDay(key: String, value: String): Seq[(String, String)] =
      validateDatePart(
        prefix = key,
        datePart = "day",
        value = value,
        requiredMessageKey = dayRequiredMessageKey,
        invalidMessageKey = dayInvalidMessageKey
      )

    private def validateMonth(key: String, value: String): Seq[(String, String)] =
      validateDatePart(
        prefix = key,
        datePart = "month",
        value = value,
        requiredMessageKey = monthRequiredMessageKey,
        invalidMessageKey = monthInvalidMessageKey
      )

    private def validateYear(key: String, value: String): Seq[(String, String)] =
      validateDatePart(
        prefix = key,
        datePart = "year",
        value = value,
        requiredMessageKey = yearRequiredMessageKey,
        invalidMessageKey = yearInvalidMessageKey
      )

    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], LocalDate] = {
      val dayErrors = validateDay(key, data(s"$key.day"))
      val monthErrors = validateMonth(key, data(s"$key.month"))
      val yearErrors = validateYear(key, data(s"$key.year"))

      val errors = Seq(dayErrors, monthErrors, yearErrors).flatten.map(error => FormError(error._1, error._2))

      if (errors.nonEmpty) {
        Left(errors)
      } else {
        Try {
          val day = data(s"$key.day").trim.toInt
          val month = data(s"$key.month").trim.toInt
          val year = data(s"$key.year").trim.toInt
          Right(LocalDate.of(year, month, day))
        } match {
          case Success(date) => date
          case _ => Left(Seq(FormError(key, dateInvalidMessageKey)))
        }
      }
    }

    override def unbind(key: String, value: LocalDate): Map[String, String] = Map(
      s"$key.day" -> value.getDayOfMonth.toString,
      s"$key.month" -> value.getMonthValue.toString,
      s"$key.year" -> value.getYear.toString
    )
  }
}
