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
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.anothertaxfrontend.models.{DataModel, EducationPageModel}
import uk.gov.hmrc.anothertaxfrontend.views.html.EducationPageView
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class EducationPageViewController @Inject()
(messagesControllerComponents: MessagesControllerComponents,
 educationPageView: EducationPageView,
)
extends FrontendController(messagesControllerComponents) {

  def educationPage: Action[AnyContent] = Action.async {  implicit request =>

    val user: DataModel = request.session.get("userData").map(data => Json.parse(data).as[DataModel]) match {
      case Some(value) => value
      case None => DataModel(None, None, None, None, None, None, None, None)
    }

    if(user.dateOfBirth.isDefined) {
      user.education match {
        case Some(value) => Future.successful(Ok(educationPageView(EducationPageModel.form.fillAndValidate(EducationPageModel(Option(value))))))
        case None => Future.successful(Ok(educationPageView(EducationPageModel.form)))
      }

    }
    else Future.successful(Redirect(uk.gov.hmrc.anothertaxfrontend.controllers.routes.PageNotFoundViewController.pageNotFound))

  }

  def educationPagePost: Action[AnyContent] = Action.async {  implicit request =>

    val user: DataModel = request.session.get("userData").map(data => Json.parse(data).as[DataModel]) match {
      case Some(value) => value
      case None => DataModel(None, None, None, None, None, None, None, None)
    }

    EducationPageModel.form.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(educationPageView(formWithErrors))),
      formData => request.body.asFormUrlEncoded.get("education").head match {
        case "No" => Future.successful(Redirect(uk.gov.hmrc.anothertaxfrontend.controllers.routes.FinishEducationPageViewController.finishEducationPage)
          .addingToSession("userData" -> Json.toJson(user.copy(education = formData.education )).toString())
        )
        case "Yes" => Future.successful(Redirect(uk.gov.hmrc.anothertaxfrontend.controllers.routes.SummaryPageViewController.summaryPage)
          .addingToSession("userData" -> Json.toJson(user.copy(education = formData.education, finishEducation = None, employmentStatus = None, annualIncome = None)).toString())
        )
      }
    )
  }

}
