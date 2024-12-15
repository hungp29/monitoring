package metrics

import metrics.Metrics.activeConnections
import org.apache.pekko.stream.Materializer
import play.api.mvc.{EssentialAction, EssentialFilter}

import javax.inject.Inject

class MetricsFilter @Inject() (implicit val mat: Materializer)
    extends EssentialFilter {

  override def apply(next: EssentialAction): EssentialAction = EssentialAction {
    requestHeader =>
      if (requestHeader.path.contains("/metrics")) {
        next(requestHeader)
      } else {
        activeConnections.inc()
        val result = next(requestHeader)

        result
          .map { response =>
            activeConnections.dec()
            response
          }(scala.concurrent.ExecutionContext.global)
          .recover { case e: Exception =>
            activeConnections.dec()
            throw e
          }(scala.concurrent.ExecutionContext.global)
      }
  }
}
