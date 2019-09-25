package com.main
//#user-registry-actor
import akka.actor.{ Actor, ActorLogging, Props }

//#user-case-classes
final case class Policy(owner: String, creditor: String, scope: Option[String])
//#user-case-classes

object PolicyRegistryActor {
  final case class ActionPerformed(description: String)
  final case object GetPolicies
  final case class CreatePolicies(policy: Policy)
  final case class DeletePolicy(owner: String)

  def props: Props = Props[PolicyRegistryActor]
}

class PolicyRegistryActor extends Actor with ActorLogging {
  import PolicyRegistryActor._

  override def receive: Receive = {
    case GetPolicies =>
      sender() ! PolicyMongoManagement.getPolicy()
      //sender() ! Users(users.toSeq)
    case CreatePolicies(user) =>{
      val result=PolicyMongoManagement.addPolicy(user)
      sender() ! ActionPerformed(s"{result}")}
    case DeletePolicy(owner) =>{
      //users.find(_.name == name) foreach { user => users -= user }
      val result=PolicyMongoManagement.deletePolicy(owner)
      sender() ! ActionPerformed(s"{result}")
    }
  }
}

