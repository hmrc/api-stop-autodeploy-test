/*
 * Copyright 2023 HM Revenue & Customs
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

package uk.gov.hmrc.controllers

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

import play.api.http.HttpErrorHandler
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents, Result}
import uk.gov.hmrc.controllers.ErrorResponse.ErrorInternalServerError
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.services.{Hello, HelloWorldService}

@Singleton
class HelloWorld @Inject() (service: HelloWorldService, httpErrorHandler: HttpErrorHandler, controllerComponents: ControllerComponents)(implicit val ec: ExecutionContext)
    extends BackendController(controllerComponents) with HeaderValidator {

  final def world: Action[AnyContent] = validateAccept(acceptHeaderValidationRules).async { implicit request =>
    result(service.fetchWorld)
  }

  final def application: Action[AnyContent] = validateAccept(acceptHeaderValidationRules).async { implicit request =>
    result(service.fetchApplication)
  }

  final def user: Action[AnyContent] = validateAccept(acceptHeaderValidationRules).async { implicit request =>
    result(service.fetchUser)
  }

  private def result: Future[Hello] => Future[Result] = {
    _.map(hello => Ok(Json.toJson(hello))).recover(recovery)
  }

  private def recovery: PartialFunction[Throwable, Result] = {
    case _ => Status(ErrorInternalServerError.httpStatusCode)(Json.toJson(ErrorInternalServerError))
  }

  override protected val cc: ControllerComponents = controllerComponents
}
