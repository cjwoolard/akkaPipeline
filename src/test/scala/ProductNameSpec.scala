import akka.actor.ActorSystem
import akka.actor.Actor
import akka.testkit.{ImplicitSender, TestKit, TestActorRef}
import java.net.URL
import java.util.Date
import org.scalatest.{WordSpecLike, MustMatchers, WordSpec}
import scala.collection.immutable.HashMap
import scala.concurrent.duration.Duration
import scala.util.Random
import scala.concurrent.duration._

class ProductNameSpec extends TestKit(ActorSystem("productNameSpec"))
with WordSpecLike
with MustMatchers
with ImplicitSender
{
  "An unresolved offer" must {
    val rule = TestActorRef[ProductNameRequiredRule]

    "a" in {
      val offer = DummyOffer("")
      rule ! Validate(offer)

      val x = receiveOne(1 second)
      expectMsg(ValidationFailed(offer, Set(ValidationFailure("Product name required"))))
    }
  }

  def DummyOffer(productName:String) = {
    val url = new URL("http://www.test.com")
    val random = Random.nextInt(10)
    val randomizedProductName = if(random%2==0) "" else productName
    val randomizedCondition = if(random%8==0) "" else "new"
    val maxPrice = 100
    val price = BigDecimal(maxPrice * Random.nextDouble()).setScale(2, BigDecimal.RoundingMode.FLOOR).toDouble
    val shippingCost = BigDecimal(maxPrice * Random.nextDouble()).setScale(2, BigDecimal.RoundingMode.FLOOR).toDouble

    val additionalAttributes = new HashMap[String, String]
    val product = new Product("Nike", randomizedProductName, "desc", additionalAttributes)
    val listing = new Listing(url, product, "seller", randomizedCondition, Set())

    new Offer(java.util.UUID.randomUUID, listing, price, shippingCost, new Date())
  }
}
