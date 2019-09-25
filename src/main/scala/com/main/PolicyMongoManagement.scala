package com.main
import com.configurations.Configurations
import reactivemongo.api.MongoConnection
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future
// Task is in monix.eval
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import akka.http.scaladsl.server
object PolicyMongoManagement {
  private def getConnection():Task[BSONCollection]={
    val mongoUri = s"mongodb://${Configurations.host}/${Configurations.db}"
    val driver = new reactivemongo.api.MongoDriver
    val connectionTask = Task.fromFuture{
      val parsedUri = MongoConnection.parseURI(mongoUri)
      val connection = parsedUri.map(driver.connection)
      val futureConnection = Future.fromTry(connection)
      futureConnection.flatMap(_.database(Configurations.db))
    }.onErrorHandleWith {
      case exception: Exception => {
        println("No fue posible conectarse a mongo", Some(exception), getClass)
        Task.raiseError(exception)
      }
    }
    dbFromConnection(connectionTask.map(x => x.connection))
  }
  private def dbFromConnection(connection: Task[MongoConnection]): Task[BSONCollection] = {
    connection.flatMap(algo => {
      Task.fromFuture {
        algo.database(Configurations.db)
          .map(_.collection(Configurations.collection))
      }
    })
  }


  def simpleInsert(coll: Task[BSONCollection], document: BSONDocument): Task[WriteResult] = {
    coll.flatMap(x => {
      Task.fromFuture(x.insert.one(document)).onErrorHandleWith {
        case exception: Exception => {
          println("no fue posible la insercion por:", Some(exception), getClass)
          Task.raiseError(exception)
        }
      }
    })
  }


  def getPolicy()= ???
  def addPolicy(policy: Policy)= {
    val document = BSONDocument(
      "owner" -> policy.owner,
       "creditor"-> policy.creditor,
      "scope"->policy.scope.getOrElse(null))
    simpleInsert(this.getConnection(), document)
      .runToFuture

  }
  def deletePolicy(owner: String)= ???
}
