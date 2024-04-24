/*
 * Copyright 2024 HM Revenue & Customs
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

package uk.gov.hmrc.euvatratesstub.controllers

import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import play.api.mvc.{Action, AnyContent, ControllerComponents}

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future
import scala.io.Source
import scala.util.Try

@Singleton()
class VatRateController @Inject()(cc: ControllerComponents)
    extends BackendController(cc) {

  def getEUVatRates: Action[AnyContent] = Action.async { implicit request =>

    val responseXml = Try(Source.fromInputStream(getClass.getResourceAsStream("/resources/xml/response.xml")).mkString)

    Future.successful(Ok(responseXml.get))
  }

}
