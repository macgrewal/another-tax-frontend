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
import uk.gov.hmrc.anothertaxfrontend.models.{AddDOBPageModel, DataModel}
import uk.gov.hmrc.anothertaxfrontend.views.html.AddDOBPageView
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class AddDOBPageViewController @Inject()
(messagesControllerComponents: MessagesControllerComponents,
 addDOBPageView: AddDOBPageView
)
extends FrontendController(messagesControllerComponents){

  def dateOfBirth: Action[AnyContent] = Action.async { implicit request=>

    val user: DataModel = request.session.get("userData").map(data => Json.parse(data).as[DataModel]) match {
        case Some(value) => value
        case None => DataModel(None, None, None, None, None, None, None, None)
      }

      if(user.firstName.isDefined && user.lastName.isDefined) {
        user.dateOfBirth match {
          case Some(value) => Future.successful(Ok(addDOBPageView(AddDOBPageModel.form.fillAndValidate(AddDOBPageModel(value.getDayOfMonth, value.getMonthValue, value.getYear)))))
          case None => Future.successful(Ok(addDOBPageView(AddDOBPageModel.form)))
        }
      } else {
        println(Console.RED_B + "Failed because session does not have a firstName and lastName value" + Console.RESET)
        Future.successful(Redirect(uk.gov.hmrc.anothertaxfrontend.controllers.routes.PageNotFoundViewController.pageNotFound))
      }


  }

  def dateOfBirthPost: Action[AnyContent] = Action.async {implicit request =>
    val user: DataModel = request.session.get("userData").map(data => Json.parse(data).as[DataModel]) match {
      case Some(value) => value
      case None => DataModel(None, None, None, None, None, None, None, None)
    }
    val formatter = DateTimeFormatter.ofPattern("d-M-yyy")

//    val dob = LocalDate.parse(s"${request.body.asFormUrlEncoded.get("day").head}-${request.body.asFormUrlEncoded.get("month").head}-${request.body.asFormUrlEncoded.get("year").head}",formatter)
//    dob.isAfter(LocalDate.now)

    AddDOBPageModel.form.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(addDOBPageView(formWithErrors))),
      formData => Future.successful(
          Redirect(uk.gov.hmrc.anothertaxfrontend.controllers.routes.EducationPageViewController.educationPage)
            .addingToSession(
              "userData" -> Json.toJson(user.copy(dateOfBirth = Option(LocalDate.parse(s"${formData.day}-${formData.month}-${formData.year}", formatter))
              )).toString()
            )
      )
    )
  }


}




