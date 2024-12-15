package controllers

import metrics.Metrics.{requestCount, requestDurationHistogram}

import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._

import java.util.concurrent.TimeUnit
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Random, Success}

/** This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject() (val controllerComponents: ControllerComponents)
    extends BaseController {

  /** Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def hello: Action[AnyContent] = Action.async { implicit request =>
    implicit val ec: ExecutionContext = controllerComponents.executionContext
    recordMetricForCallback("hello") {
      Future {
        Thread.sleep(Random.nextInt(1000))
        Ok(Json.obj("value" -> "Hello World"))
      }
    }
  }

  def random: Action[AnyContent] = Action.async { implicit request =>
    implicit val ec: ExecutionContext = controllerComponents.executionContext
    recordMetricForCallback("random") {
      Future {
        val randomValue = Random.nextInt(600)
        Thread.sleep(randomValue)
        if (randomValue > 500) {
          throw new Exception("Random error")
        }
        Ok(Json.obj("value" -> randomValue))
      }
    }
  }

  def recordMetricForCallback[T](func: String)(
      future: => Future[T]
  )(implicit ec: ExecutionContext): Future[T] = {

    def apiTime(baseTime: Long): Double = {
      val t2 = System.nanoTime
      TimeUnit.NANOSECONDS.toSeconds(t2 - baseTime).toDouble
    }

    requestCount.labels(func).inc()
    val startTime = System.nanoTime
    val returnFuture = future

    future.onComplete {
      case Success(_) =>
        requestDurationHistogram
          .labels(func, "success")
          .observe(apiTime(startTime))
      case Failure(_) =>
        requestDurationHistogram
          .labels(func, "error")
          .observe(apiTime(startTime))
    }

    returnFuture
  }

}
