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

import org.scalatest.matchers.must.Matchers
import uk.gov.hmrc.euvatratesstub.base.BaseSpec

import scala.xml.Elem

class EuVatRatesXmlServiceSpec extends BaseSpec with Matchers {

  "EuVatRatesXmlService" - {

    "filter out rates for countries not in the requested list" in {

      val service = new EuVatRatesXmlService()

      val requestedCountries = Seq("AT")

      val responseXml: Elem = service.getRatesResponse(requestedCountries)

      val countryCodesInResponse = (responseXml \\ "memberState").map(_.text)

      countryCodesInResponse must contain only "AT"
      countryCodesInResponse must not contain "FR"
    }

    "return an empty response when no matching countries are requested" in {

      val service = new EuVatRatesXmlService()

      val requestedCountries = Seq("XX")

      val responseXml: Elem = service.getRatesResponse(requestedCountries)

      (responseXml \\ "vatRateResults").isEmpty mustBe true
    }

    "handle an empty list of requested countries gracefully" in {

      val service = new EuVatRatesXmlService()

      val requestedCountries = Seq()

      val responseXml: Elem = service.getRatesResponse(requestedCountries)

      (responseXml \\ "vatRateResults").isEmpty mustBe true
    }
  }
}
