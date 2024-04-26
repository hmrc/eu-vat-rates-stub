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

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.euvatratesstub.base.BaseSpec
import uk.gov.hmrc.euvatratesstub.utils.EuVatRatesXmlService

class VatRateControllerSpec extends BaseSpec {

  private val mockEuVatRateXmlService = mock[EuVatRatesXmlService]

  private val exampleRequestXml = <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                                             xmlns:urn="urn:ec.europa.eu:taxud:tedb:services:v1:IVatRetrievalService"
                                             xmlns:urn1="urn:ec.europa.eu:taxud:tedb:services:v1:IVatRetrievalService:types">
    <soapenv:Header/>
    <soapenv:Body>
      <urn:retrieveVatRatesReqMsg>
        <urn1:memberStates>
          <!--1 or more repetitions:-->
          <urn1:isoCode>AT</urn1:isoCode>
        </urn1:memberStates>
        <urn1:from>2024-01-01</urn1:from>
        <urn1:to>2025-01-01</urn1:to>
      </urn:retrieveVatRatesReqMsg>
    </soapenv:Body>
  </soapenv:Envelope>

  private val exampleResponseXml = <env:Envelope xmlns:env="http://schemas.xmlsoap.org/soap/envelope/">
    <env:Header/>
    <env:Body>
      <ns0:retrieveVatRatesRespMsg xmlns="urn:ec.europa.eu:taxud:tedb:services:v1:IVatRetrievalService:types"
                                   xmlns:ns0="urn:ec.europa.eu:taxud:tedb:services:v1:IVatRetrievalService">
        <additionalInformation/>
        <vatRateResults>
          <memberState>AT</memberState>
          <type>REDUCED</type>
          <rate>
            <type>REDUCED_RATE</type>
            <value>10.0</value>
          </rate>
          <situationOn>2024-01-01+01:00</situationOn>
        </vatRateResults>
      </ns0:retrieveVatRatesRespMsg>
    </env:Body>
  </env:Envelope>


  "POST /" - {
    "return 200" in {

      when(mockEuVatRateXmlService.getRatesResponse(any())) thenReturn exampleResponseXml

      val app =
        applicationBuilder
          .overrides(bind[EuVatRatesXmlService].toInstance(mockEuVatRateXmlService))
          .build()

      running(app) {

        val request =
          FakeRequest(POST, routes.VatRateController.getEUVatRates.url)
            .withXmlBody(exampleRequestXml)

        val result = route(app, request).value

        status(result) mustEqual OK
      }
    }
  }
}
