package controllers

import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.common.TextFormat
import io.prometheus.client.hotspot.DefaultExports
import play.api.mvc.{
  AbstractController,
  Action,
  AnyContent,
  ControllerComponents,
  Request
}

import java.io.StringWriter
import javax.inject.{Inject, Singleton}

@Singleton
class MetricController @Inject() (cc: ControllerComponents)
    extends AbstractController(cc) {
  DefaultExports.initialize()

  def metrics: Action[AnyContent] = Action {
    implicit request: Request[AnyContent] =>
      val writer = new StringWriter()
      TextFormat.write004(
        writer,
        CollectorRegistry.defaultRegistry.metricFamilySamples()
      )
      Ok(writer.toString).as("text/plain")
  }
}
