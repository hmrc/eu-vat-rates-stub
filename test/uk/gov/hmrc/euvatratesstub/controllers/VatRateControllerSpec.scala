package uk.gov.hmrc.euvatratesstub.controllers

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}

class VatRateControllerSpec extends AnyWordSpec with Matchers {

  private val fakeRequest = FakeRequest("GET", "/")
  private val controller = new VatRateController(Helpers.stubControllerComponents())

  "GET /" should {
    "return 200" in {
      val result = controller.getEUVatRates()(fakeRequest)
      status(result) shouldBe Status.OK
    }
  }
}
