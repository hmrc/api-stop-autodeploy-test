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

package uk.gov.hmrc.apistopautodeploytest.controllers

import play.api.http.Status.{BAD_REQUEST, INTERNAL_SERVER_ERROR, NOT_ACCEPTABLE, NOT_FOUND, UNAUTHORIZED}
import play.api.libs.json.{Json, Writes}

sealed abstract class ErrorResponse(val httpStatusCode: Int, val errorCode: String, val message: String) extends Serializable

object ErrorResponse {

  case object ErrorUnauthorized extends ErrorResponse(UNAUTHORIZED, "UNAUTHORIZED", "Bearer token is missing or not authorized")

  case object ErrorNotFound extends ErrorResponse(NOT_FOUND, "NOT_FOUND", "Resource was not found")

  case object ErrorGenericBadRequest extends ErrorResponse(BAD_REQUEST, "BAD_REQUEST", "Bad Request")

  case object ErrorAcceptHeaderInvalid extends ErrorResponse(NOT_ACCEPTABLE, "ACCEPT_HEADER_INVALID", "The accept header is missing or invalid")

  case object ErrorInternalServerError extends ErrorResponse(INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "Internal server error")

  implicit val w1: Writes[ErrorAcceptHeaderInvalid.type] = {
    case d @ _ => Json.obj(
        "code"    -> d.errorCode,
        "message" -> d.message
      )
  }

  implicit val w2: Writes[ErrorInternalServerError.type] = {
    case d @ _ => Json.obj(
        "code"    -> d.errorCode,
        "message" -> d.message
      )
  }
}
