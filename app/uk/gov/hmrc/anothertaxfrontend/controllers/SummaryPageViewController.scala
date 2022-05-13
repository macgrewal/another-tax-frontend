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
import uk.gov.hmrc.anothertaxfrontend.models.DataModel
import uk.gov.hmrc.anothertaxfrontend.views.html.SummaryPageView
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future
import scala.util.Random


@Singleton
class SummaryPageViewController @Inject()
(messagesControllerComponents: MessagesControllerComponents,
 summaryPageView: SummaryPageView)
extends FrontendController(messagesControllerComponents) {

  def summaryPage: Action[AnyContent] = Action.async { implicit request =>

      val user: DataModel = request.session.get("userData").map(data => Json.parse(data).as[DataModel]) match {
        case Some(value) => value
        case None => DataModel(None, None, None, None, None, None, None, None)
      }

      if(user.firstName.isDefined && user.lastName.isDefined && user.dateOfBirth.isDefined && user.education.contains("Yes") ||
          user.education.contains("No") && user.finishEducation.isDefined && user.employmentStatus.contains("Unemployed") ||
          !user.employmentStatus.contains("Unemployed") && user.annualIncome.isDefined) {

        Future.successful(Ok(summaryPageView(user)))

      } else {
        Future.successful(Redirect(uk.gov.hmrc.anothertaxfrontend.controllers.routes.PageNotFoundViewController.pageNotFound))
      }


  }

  def summaryPagePost: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Redirect(uk.gov.hmrc.anothertaxfrontend.controllers.routes.ConfirmationPageViewController.confirmationPage)
      .addingToSession("referenceNumber" -> Random.alphanumeric.take(8).mkString("").toUpperCase))
  }

}
