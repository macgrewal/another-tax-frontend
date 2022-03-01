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
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.anothertaxfrontend.forms.OptionsForm
import uk.gov.hmrc.anothertaxfrontend.views.html.EmploymentStatusPage
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class EmploymentStatusController @Inject()(mcc: MessagesControllerComponents,
                                           view: EmploymentStatusPage)
  extends FrontendController(mcc)
    with SessionStorageController[String] {

  override val sessionKey: String = "employmentStatus"

  val validOptions: Seq[String] = Seq("fullTime", "partTime", "unemployed")
  val employmentStatusForm: Form[String] = OptionsForm.create("employmentStatus.required", validOptions)

  val display: Action[AnyContent] = Action.async { implicit request =>
    val form = populateFormFromSession(employmentStatusForm)
    Future.successful(Ok(view(form)))
  }

  val submit: Action[AnyContent] = Action.async { implicit request =>
    val result = employmentStatusForm.bindFromRequest().fold(
      formWithErrors => BadRequest(view(formWithErrors)),
      data => saveSessionData(data) {
        Redirect(routes.EmploymentStatusController.display)
      }
    )

    Future.successful(result)
  }
}
