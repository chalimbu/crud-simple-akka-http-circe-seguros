package com.main
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import akka.http.scaladsl.server.{Directives, Route}
import akka.routing.Router

trait Router {
  def route: Route
}

class PolicyRoutes() extends Router with Directives with PolicyDirectives {
  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  override def route: Route =
    pathPrefix("Policy") {
      concat(
        pathEnd{
          concat(
            post{
              entity(as[Policy]) { Policy =>
                PolicyValidator.validate(Policy) match {
                  case Some(apiError) =>
                    complete(apiError.statusCode, apiError.message)
                  case None =>
                    //.save(createTodo)
                    handleWithGeneric(PolicyMongoManagement.addPolicy(Policy)) { pools =>
                      complete(pools)
                    }
                }
              }
            }
          )
        }
      )
    }
}
