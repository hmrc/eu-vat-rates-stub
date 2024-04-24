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

    val responseXml = Try(Source.fromInputStream(getClass.getResourceAsStream("/resources/examples/example-request.xml")).mkString)

    Future.successful(Ok(responseXml.get))
  }

}
