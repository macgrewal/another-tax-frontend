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
import uk.gov.hmrc.anothertaxfrontend.views.html.ConfirmationPageView
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import java.time.LocalDate
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class ConfirmationPageViewController @Inject()
(messagesControllerComponents: MessagesControllerComponents,
 confirmationPageView: ConfirmationPageView)
  extends FrontendController(messagesControllerComponents) {


    def confirmationPage: Action[AnyContent] = Action.async { implicit request =>


      val user: DataModel = request.session.get("userData").map(data => Json.parse(data).as[DataModel]) match {
        case Some(value) => value
        case None => DataModel(None, None, None, None, None, None, None, None)
      }

      var annualIncome: BigDecimal = 0
      annualIncome = (request.session.get("userData").map(data => Json.parse(data).as[DataModel]) match {
        case Some(value) => value.annualIncome
        case None => None
      }) match {
        case Some(value) => value
        case None => 0
      }

      val referenceNumber = request.session.get("referenceNumber")

//      val tax = "You do not qualify for this tax since you are still in education"
//      Future.successful(Ok(confirmationPageView(tax, referenceNumber)))



       if(referenceNumber.isDefined && (LocalDate.now().getYear - user.dateOfBirth.get.getYear < 18)) {
        val tax = "You owe no tax since you are under 18"
        Future.successful(Ok(confirmationPageView(tax, referenceNumber)))
       } else if (referenceNumber.isDefined && user.education.contains("Yes")) {
         val tax = "You do not qualify for this tax since you are still in education"
         Future.successful(Ok(confirmationPageView(tax, referenceNumber)))
      } else if (referenceNumber.isDefined && user.employmentStatus.contains("Unemployed")) {
        val tax = "You owe no tax since you are unemployed"
        Future.successful(Ok(confirmationPageView(tax, referenceNumber)))
      } else if (referenceNumber.isDefined && user.employmentStatus.contains("Part time employment")) {
        val tax = s"You owe £ ${BigDecimal(annualIncome.toString.toDouble * 0.05).setScale(2,BigDecimal.RoundingMode.HALF_UP)} of tax"
        Future.successful(Ok(confirmationPageView(tax, referenceNumber)))
      } else if (referenceNumber.isDefined && user.employmentStatus.contains("Full time employment")) {
         val tax = s"You owe £ ${BigDecimal(annualIncome.toString.toDouble * 0.10).setScale(2,BigDecimal.RoundingMode.HALF_UP)} of tax"
        Future.successful(Ok(confirmationPageView(tax, referenceNumber)))
      } else {
        Future.successful(Redirect(uk.gov.hmrc.anothertaxfrontend.controllers.routes.PageNotFoundViewController.pageNotFound))
      }


    }

}
