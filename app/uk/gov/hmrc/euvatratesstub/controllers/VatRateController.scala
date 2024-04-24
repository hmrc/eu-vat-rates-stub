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

  private val responseXml = Try(Source.fromInputStream(getClass.getResourceAsStream("/resources/xml/response.xml")).mkString)

  def getEUVatRates: Action[AnyContent] = Action.async { implicit request =>

    val requestBody = request.body.asXml.get

    val requestedCountryCodes = (requestBody \\ "Envelope" \\ "Body" \\ "memberStates").map { memberStateElem =>
      val isoCodeElem = memberStateElem \ "isoCode"

      isoCodeElem.text
    }

    val loadXml = scala.xml.XML.loadString(responseXml.get)

    val allRates = (loadXml \\ "Envelope" \\ "Body" \\ "retrieveVatRatesRespMsg" \\ "vatRateResults").filter { vatRateElem =>
      val memberStateElem = vatRateElem \ "memberState"
      requestedCountryCodes.contains(memberStateElem.text)
    }

    val response = <env:Envelope xmlns:env="http://schemas.xmlsoap.org/soap/envelope/">
      <env:Header/>
      <env:Body>
        <ns0:retrieveVatRatesRespMsg xmlns="urn:ec.europa.eu:taxud:tedb:services:v1:IVatRetrievalService:types" xmlns:ns0="urn:ec.europa.eu:taxud:tedb:services:v1:IVatRetrievalService">
          <additionalInformation/>
          {allRates}
        </ns0:retrieveVatRatesRespMsg>
      </env:Body>
    </env:Envelope>

    Future.successful(Ok(response.toString()))
  }

}
