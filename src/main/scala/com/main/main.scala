package com.main

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext

object main {
  // set up ActorSystem and other dependencies here
  //#main-class
  //#server-bootstrapping
  implicit val system: ActorSystem = ActorSystem("akkaCrud")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  //#server-bootstrapping

  val userRegistryActor: ActorRef = system.actorOf(PolicyRegistryActor.props, "userRegistryActor")

  //#main-class
  // from the UserRoutes trait
  lazy val routes: Route = PolicyRoutes
  //val route = concat(userRoutes, healthCheckRoutes, ...)
  //#main-class


}
