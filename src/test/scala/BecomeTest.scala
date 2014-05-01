import akka.actor.{ActorRef, Props, ActorSystem, Actor}
import akka.testkit.TestActor.AutoPilot
import akka.testkit.{TestActor, ImplicitSender, TestKit, TestActorRef}
import java.net.URL
import java.util.Date
import org.scalatest.{WordSpecLike, MustMatchers}
import scala.collection.immutable.HashMap
import scala.concurrent.duration._

case class Ping
case class Pong

class TestActor extends Actor{
  def receive = {
    case Ping => {
      println("Ping: " + sender)
      context become pong(sender, 3)
    }
  }

  def pong(requestor:ActorRef, count:Int) : Receive = {
    case Pong => {
      context become pong(requestor, count-1)
      println("Pong:" + count + " " + sender)
    }
  }
}

class BecomeTest extends  TestKit(ActorSystem("productNameSpec"))
with WordSpecLike
with MustMatchers
with ImplicitSender {

  "Valid Condition" in {
    val offer = Offer.DummyOffer(new URL("http://www.aa.com"), "asdf")
    val rules = TestActorRef(Props[ConditionRules])
    rules ! Validate(offer)

    expectMsg(ValidationPassed(offer))
  }

  "Invalid Condition" in {
    val product = new Product("Nike", "productOne", "desc", new HashMap[String,Any]())
    val listing = new Listing(new URL("http://www.aa.com") , product, "")
    val offer = new Offer(java.util.UUID.randomUUID, listing, 1.23, new Date())

    val rules = TestActorRef(Props[ConditionRules])

    setAutoPilot(new TestActor.AutoPilot {
      def run(sender: ActorRef, msg: Any) = {
        println(sender + " " + msg)
        this
      }
    })

    rules ! Validate(offer)

    expectMsg(ValidationFailed(offer,Set(ValidationFailure("Condition required"), ValidationFailure("'' is not in expected set [New, Used]"))))
  }

//  "a" in {
//    val a = TestActorRef(Props[TestActor])
//    a ! Ping
//    a ! Pong
//    a ! Pong
//    a ! Pong
//    a ! Pong
//  }
}
