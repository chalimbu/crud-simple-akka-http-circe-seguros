package com.main
import scala.concurrent.Future
import scala.util.{Failure, Success}
import akka.http.scaladsl.server.{Directive1, Directives}
import com.configurations.ApiErrorConfig

trait PolicyDirectives extends Directives {
  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  def handle[T]
    (f: Future[T])
    (e: Throwable => ApiErrorConfig): Directive1[T] = onComplete(f) flatMap {
      case Success(t) =>
        provide(t)
      case Failure(error) =>
        val apiError = e(error)
        complete(apiError.statusCode, apiError.message)
  }

  def handleWithGeneric[T](f: Future[T]): Directive1[T] =
    handle[T](f)(_ => ApiErrorConfig.generic)

}
