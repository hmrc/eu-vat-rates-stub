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
