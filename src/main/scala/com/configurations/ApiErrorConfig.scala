package com.configurations
import akka.http.scaladsl.model.{StatusCode, StatusCodes}

final case class ApiErrorConfig private(statusCode: StatusCode, message: String)

object ApiErrorConfig {
  private def apply(statusCode: StatusCode, message: String): ApiErrorConfig = {
    new ApiError(statusCode, message)
  }
  val generic: ApiErrorConfig =
    new ApiErrorConfig(StatusCodes.InternalServerError, "Unknown error.")

  val missingFields: ApiErrorConfig =
    new ApiErrorConfig(StatusCodes.BadRequest, "The fields are incorrect")
}