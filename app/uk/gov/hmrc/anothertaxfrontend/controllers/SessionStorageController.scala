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

import play.api.data.Form
import play.api.libs.json.{Json, Reads, Writes}
import play.api.mvc.{AnyContent, Request, Result}

trait SessionStorageController[T] {

  val sessionKey: String

  def populateFormFromSession(form: Form[T])
                             (implicit request: Request[AnyContent],
                              jsonReads: Reads[T]): Form[T] = {
    request.session.data.get(sessionKey).fold(form) { data =>
      val name = Json.parse(data).as[T]
      form.fillAndValidate(name)
    }
  }

  def saveSessionData(data: T)(navigate: Result)
                     (implicit request: Request[AnyContent],
                      jsonWrites: Writes[T]): Result = {
    navigate.addingToSession(sessionKey -> Json.toJson(data).toString())
  }
}
