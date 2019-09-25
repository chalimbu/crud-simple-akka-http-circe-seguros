package com.main

import akka.actor.Status.Success
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Route

import scala.concurrent.{ExecutionContext, Future}
import com.main.PolicyRoutes
import monix.eval.Task
import akka.http.scaladsl.Http
import com.configurations.Configurations
import monix.execution.CancelableFuture
import monix.execution.Scheduler.Implicits.global

object main extends App with PolicyRoutes {
  // set up ActorSystem and other dependencies here
  //#main-class
  //#server-bootstrapping
  implicit val system: ActorSystem = ActorSystem("akkaCrud")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  //#server-bootstrapping


  //#main-class
  // from the UserRoutess trait
  val routes: Route = route
  //val route = concat(userRoutes, healthCheckRoutes, ...)
  //#main-class
  val server: Server=new Server(routes,Configurations.hostWithoutPort,Configurations.port)
  val binding=server.bind
  val bound: CancelableFuture[Http.ServerBinding] =binding.runToFuture
  bound.map(x=>{println(s"server runing at ${x.localAddress.getHostString} " +
    s"port ${x.localAddress.getPort}")})
  //#http-server


}
