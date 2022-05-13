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
import uk.gov.hmrc.anothertaxfrontend.models.{DataModel, EmploymentStatusPageModel}
import uk.gov.hmrc.anothertaxfrontend.views.html.EmploymentStatusPageView
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class EmploymentStatusPageViewController @Inject()
(messagesControllerComponents: MessagesControllerComponents,
 employmentStatusPageView: EmploymentStatusPageView)
extends FrontendController(messagesControllerComponents) {

  def employmentStatusPage: Action[AnyContent] = Action.async { implicit request =>


      val user: DataModel = request.session.get("userData").map(data => Json.parse(data).as[DataModel]) match {
        case Some(value) => value
        case None => DataModel(None, None, None, None, None, None, None, None)
      }

      if(user.education.contains("No") && user.finishEducation.isDefined) {
        user.employmentStatus match {
          case None => Future.successful(Ok(employmentStatusPageView(EmploymentStatusPageModel.form)))
          case Some(value) => Future.successful(Ok(employmentStatusPageView(EmploymentStatusPageModel.form.fillAndValidate(EmploymentStatusPageModel(Option(value))))))
        }

      }
      else Future.successful(Redirect(uk.gov.hmrc.anothertaxfrontend.controllers.routes.PageNotFoundViewController.pageNotFound))


  }

  def employmentStatusPagePost: Action[AnyContent] = Action.async { implicit request =>

    val user: DataModel = request.session.get("userData").map(data => Json.parse(data).as[DataModel]) match {
      case Some(value) => value
      case None => DataModel(None, None, None, None, None, None, None, None)
    }

    EmploymentStatusPageModel.form.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(employmentStatusPageView(formWithErrors))),
      formData => request.body.asFormUrlEncoded.get("employmentStatus").head match {
        case "Unemployed" => Future.successful(
          Redirect(uk.gov.hmrc.anothertaxfrontend.controllers.routes.SummaryPageViewController.summaryPage)
            .addingToSession("userData" -> Json.toJson(user.copy(employmentStatus = formData.employmentStatus, annualIncome = None)).toString())
        )
        case _ => Future.successful(
          Redirect(uk.gov.hmrc.anothertaxfrontend.controllers.routes.AnnualIncomePageViewController.annualIncomePage)
            .addingToSession("userData" -> Json.toJson(user.copy(employmentStatus = formData.employmentStatus)).toString())
        )
      }
    )
  }

}
