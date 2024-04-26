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

package uk.gov.hmrc.euvatratesstub.utils

import scala.io.Source
import scala.util.Try
import scala.xml.Elem

class EuVatRatesXmlService {

  private val responseXml = Try(Source.fromInputStream(getClass.getResourceAsStream("/resources/xml/response.xml")).mkString)

  private val loadXml = scala.xml.XML.loadString(responseXml.get)

  def getRatesResponse(requestedCountryCodes: Seq[String]): Elem = {
    val allRates = (loadXml \\ "Envelope" \\ "Body" \\ "retrieveVatRatesRespMsg" \\ "vatRateResults").filter { vatRateElem =>
      val memberStateElem = vatRateElem \ "memberState"
      requestedCountryCodes.contains(memberStateElem.text)
    }

    <env:Envelope xmlns:env="http://schemas.xmlsoap.org/soap/envelope/">
      <env:Header/>
      <env:Body>
        <ns0:retrieveVatRatesRespMsg xmlns="urn:ec.europa.eu:taxud:tedb:services:v1:IVatRetrievalService:types" xmlns:ns0="urn:ec.europa.eu:taxud:tedb:services:v1:IVatRetrievalService">
          <additionalInformation/>{allRates}
        </ns0:retrieveVatRatesRespMsg>
      </env:Body>
    </env:Envelope>
  }

}
