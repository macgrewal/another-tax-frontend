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
import uk.gov.hmrc.anothertaxfrontend.models.{AddNamePageModel, DataModel}
import uk.gov.hmrc.anothertaxfrontend.views.html.AddNamePageView
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class AddNamePageViewController @Inject()(
                                        mcc: MessagesControllerComponents,
                                        addNamePageView: AddNamePageView)
  extends FrontendController(mcc) {
  def addNamePage: Action[AnyContent] = Action.async { implicit request =>
    val form = request.session.data.get("userData").fold(AddNamePageModel.form) {data =>
      val name = Json.parse(data).as[AddNamePageModel]
      AddNamePageModel.form.fillAndValidate(name)
    }
    Future.successful(Ok(addNamePageView(form)))
  }



  def addNamePagePost: Action[AnyContent] = Action.async {implicit request =>
    val user: DataModel = request.session.get("userData").map(data => Json.parse(data).as[DataModel]) match {
      case Some(value) => value
      case None => DataModel(None, None, None, None, None, None, None, None)
    }
    AddNamePageModel.form.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(addNamePageView(formWithErrors))),
      formData => Future.successful(Redirect(uk.gov.hmrc.anothertaxfrontend.controllers.routes.AddDOBPageViewController.dateOfBirth)
        .addingToSession("userData" -> Json.toJson(user.copy(Option(formData.firstName), formData.middleName, Option(formData.lastName))).toString()))
      )
  }
}
