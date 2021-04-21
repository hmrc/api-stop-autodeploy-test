/*
 * Copyright 2021 HM Revenue & Customs
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

package unit.uk.gov.hmrc.controllers

import akka.stream.Materializer
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.OneAppPerSuite
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.HttpErrorHandler
import play.api.http.Status.{INTERNAL_SERVER_ERROR, NOT_ACCEPTABLE, OK}
import play.api.mvc.Result
import play.api.test.{FakeRequest, StubControllerComponentsFactory}
import uk.gov.hmrc.controllers.HelloWorld
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.test.UnitSpec
import uk.gov.hmrc.services.{Hello, HelloWorldService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future.{failed, successful}

class HelloWorldSpec extends UnitSpec with MockitoSugar with GuiceOneAppPerSuite with StubControllerComponentsFactory {

  trait Setup {
    implicit val mat: Materializer = app.materializer
    val validRequest = FakeRequest().withHeaders("Accept" -> "application/vnd.hmrc.1.0+json")
    val invalidRequest = FakeRequest()
    val mockHelloWorldService: HelloWorldService = mock[HelloWorldService]
    val httpErrorHandler =  mock[HttpErrorHandler]
    val underTest = new HelloWorld(mockHelloWorldService, httpErrorHandler, stubControllerComponents())
  }

  "world" should {
    "return message from service" in new Setup {
      val expectedMessage = "hello world"
      when(mockHelloWorldService.fetchWorld(any[HeaderCarrier])).thenReturn(successful(Hello(expectedMessage)))

      val result: Result = await(underTest.world(validRequest))

      status(result) shouldEqual OK
      (jsonBodyOf(result) \ "message").as[String] shouldBe expectedMessage
    }

    "fail with status 406 when request doesn't have an appropriate header" in new Setup {
      val result: Result = await(underTest.world(invalidRequest))

      status(result) shouldEqual NOT_ACCEPTABLE
    }

    "fail with 500 when an unexpected error happens" in new Setup {
      when(mockHelloWorldService.fetchWorld(any[HeaderCarrier])).thenReturn(failed(new RuntimeException("something went wrong")))

      val result: Result = await(underTest.world(validRequest))

      status(result) shouldEqual INTERNAL_SERVER_ERROR
    }
  }

  "application" should {
    "return message from service" in new Setup {
      val expectedMessage = "hello application"
      when(mockHelloWorldService.fetchApplication(any[HeaderCarrier])).thenReturn(successful(Hello(expectedMessage)))

      val result: Result = await(underTest.application(validRequest))

      status(result) shouldEqual OK
      (jsonBodyOf(result) \ "message").as[String] shouldBe expectedMessage
    }

    "fail with status 406 when request doesn't have an appropriate header" in new Setup {
      val result: Result = await(underTest.application(invalidRequest))

      status(result) shouldEqual NOT_ACCEPTABLE
    }

    "fail with 500 when an unexpected error happens" in new Setup {
      when(mockHelloWorldService.fetchApplication(any[HeaderCarrier])).thenReturn(failed(new RuntimeException("something went wrong")))

      val result: Result = await(underTest.application(validRequest))

      status(result) shouldEqual INTERNAL_SERVER_ERROR
    }
  }

  "user" should {
    "return message from service" in new Setup {
      val expectedMessage = "hello user"
      when(mockHelloWorldService.fetchUser(any[HeaderCarrier])).thenReturn(successful(Hello(expectedMessage)))

      val result: Result = await(underTest.user(validRequest))

      status(result) shouldEqual OK
      (jsonBodyOf(result) \ "message").as[String] shouldBe expectedMessage
    }

    "fail with status 406 when request doesn't have an appropriate header" in new Setup {
      val result: Result = await(underTest.user(invalidRequest))

      status(result) shouldEqual NOT_ACCEPTABLE
    }

    "fail with 500 when an unexpected error happens" in new Setup {
      when(mockHelloWorldService.fetchUser(any[HeaderCarrier])).thenReturn(failed(new RuntimeException("something went wrong")))

      val result: Result = await(underTest.user(validRequest))

      status(result) shouldEqual INTERNAL_SERVER_ERROR
    }
  }
}
