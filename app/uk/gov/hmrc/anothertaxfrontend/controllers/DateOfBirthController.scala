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
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.anothertaxfrontend.forms.DateForm
import uk.gov.hmrc.anothertaxfrontend.views.html.DateOfBirthPage
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import java.time.LocalDate
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class DateOfBirthController @Inject()(mcc: MessagesControllerComponents,
                                      view: DateOfBirthPage)
  extends FrontendController(mcc) {

  val dateOfBirthForm: Form[LocalDate] = DateForm.create(
    "dob.day.required",
    "dob.day.invalid",
    "dob.month.required",
    "dob.month.invalid",
    "dob.year.required",
    "dob.year.invalid",
    "dob.invalid"
  )

  val display: Action[AnyContent] = Action.async { implicit request =>
    val form = request.session.data.get("dateOfBirth").fold(dateOfBirthForm) { data =>
      val dob = Json.parse(data).as[LocalDate]
      dateOfBirthForm.fillAndValidate(dob)
    }
    Future.successful(Ok(view(form)))
  }

  val submit: Action[AnyContent] = Action.async { implicit request =>
    val result = dateOfBirthForm.bindFromRequest().fold(
      formWithErrors => BadRequest(view(formWithErrors)),
      data =>
        Redirect(uk.gov.hmrc.anothertaxfrontend.controllers.routes.DateOfBirthController.display)
          .addingToSession("dateOfBirth" -> Json.toJson(data).toString())
    )

    Future.successful(result)
  }
}
