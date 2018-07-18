/*
 * Copyright 2018 HM Revenue & Customs
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

import controllers.errorResponseWrites
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Result}
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.play.microservice.controller.BaseController
import uk.gov.hmrc.services.{Hello, HelloWorldService, LiveService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait HelloWorld extends BaseController with HeaderValidator {

  val service: HelloWorldService

  implicit val hc: HeaderCarrier

  final def world: Action[AnyContent] = validateAccept(acceptHeaderValidationRules).async {
    result(service.fetchWorld)
  }

  final def application: Action[AnyContent] = validateAccept(acceptHeaderValidationRules).async {
    result(service.fetchApplication)
  }

  final def user: Action[AnyContent] = validateAccept(acceptHeaderValidationRules).async {
    result(service.fetchUser)
  }

  private def result: Future[Hello] => Future[Result] = {
    _.map(hello => Ok(Json.toJson(hello))).recover(recovery)
  }

  private def recovery: PartialFunction[Throwable, Result] = {
    case _ => Status(ErrorInternalServerError.httpStatusCode)(Json.toJson(ErrorInternalServerError))
  }

}

object LiveController extends HelloWorld {
  override val service = LiveService
  override implicit val hc: HeaderCarrier = HeaderCarrier()
}
