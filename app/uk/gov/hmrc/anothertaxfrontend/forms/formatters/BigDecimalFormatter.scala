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

import scala.util.{Failure, Success, Try}

object BigDecimalFormatter {
  def formatter(requiredMessageKey: String,
                invalidMessageKey: String): Formatter[BigDecimal] = new Formatter[BigDecimal] {

    override def bind(key: String,
                      data: Map[String, String]): Either[Seq[FormError], BigDecimal] = {
      val input = data.getOrElse(key, "").trim

      Try(BigDecimal(input)) match {
        case Success(value) => Right(value)
        case Failure(_) if input.isEmpty => Left(Seq(FormError(key, requiredMessageKey)))
        case Failure(_) => Left(Seq(FormError(key, invalidMessageKey)))
      }
    }

    override def unbind(key: String, value: BigDecimal): Map[String, String] = Map(key -> value.toString())
  }
}
