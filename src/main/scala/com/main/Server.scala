package com.main


import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import monix.eval.Task

import scala.concurrent.{ExecutionContext, Future}
//implict akka http dependencies
class Server(router: Route,host: String,port: Int)(implicit system: ActorSystem,ex: ExecutionContext,mat: ActorMaterializer){
  def bind: Task[ServerBinding]={
    Task.fromFuture{Http().bindAndHandle(router,host,port)
    }onErrorHandleWith {
      case exception: Exception => {
        println("No fue posible crear el servidor", Some(exception), getClass)
        Task.raiseError(exception)
      }
    }

  }
}