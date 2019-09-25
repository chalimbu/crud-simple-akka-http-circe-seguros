package com.main
import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import akka.http.scaladsl.server.{Directives, Route}
import akka.routing.Router
import akka.util.Timeout
import scala.concurrent.duration._

trait Router {
  def route: Route
}

trait PolicyRoutes extends Router with Directives  with PolicyDirectives{
  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._
  //#user-routes-class

  // we leave these abstract, since they will be provided by the App
  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[PolicyRoutes])

  // other dependencies that UserRoutes use
  //def userRegistryActor: ActorRef

  // Required by the `ask` (?) method below
  implicit lazy val timeout = Timeout(5.seconds) // usually we'd obtain the timeout from the system's configuration

  override def route: Route = pathPrefix("Policy") {
    concat(
      pathEnd{
        concat(
          post{
            entity(as[Policy]) { policyInp =>
              PolicyValidator.validate(policyInp) match {
                case Some(apiError) =>
                  complete(apiError.statusCode, apiError.message)
                case None =>{
                  PolicyMongoManagement.addPolicy(policyInp)
                  complete("Policy addec correctly")
                }
                  //.save(createTodo)
                  //handleWithGeneric(PolicyMongoManagement.addPolicy(policyInp)){ result =>
              }
            }
          }
        )
      }
    )
  }
}

