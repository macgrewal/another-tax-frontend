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

import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.anothertaxfrontend.forms.FullNameForm
import uk.gov.hmrc.anothertaxfrontend.models.FullName
import uk.gov.hmrc.anothertaxfrontend.views.html.FullNamePage
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class FullNameController @Inject()(mcc: MessagesControllerComponents,
                                   view: FullNamePage,
                                   nameForm: FullNameForm)
  extends FrontendController(mcc) {

  val display: Action[AnyContent] = Action.async { implicit request =>
    val form = request.session.data.get("fullName").fold(nameForm.form) { data =>
      val name = Json.parse(data).as[FullName]
      nameForm.form.fillAndValidate(name)
    }
    Future.successful(Ok(view(form)))
  }

  val submit: Action[AnyContent] = Action.async { implicit request =>
    val result = nameForm.form.bindFromRequest().fold(
      formWithErrors => BadRequest(view(formWithErrors)),
      data =>
        Redirect(uk.gov.hmrc.anothertaxfrontend.controllers.routes.FullNameController.display)
          .withSession("fullName" -> Json.toJson(data).toString())
    )

    Future.successful(result)
  }
}
