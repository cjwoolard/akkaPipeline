import akka.actor.ActorSystem
import akka.actor.Actor
import akka.testkit.{TestKit, TestActorRef}
import java.net.URL
import java.util.Date
import org.scalatest.MustMatchers
import org.scalatest.WordSpec
import scala.collection.immutable.HashMap
import scala.util.Random

class ProductNameSpec extends TestKit(ActorSystem("productNameSpec"))
with WordSpec
with MustMatchers
{
  "An unresolved offer" must {
    val rule = TestActorRef[ProductNameRequiredRule]

    "a" in {
      val offer = DummyOffer("")
      rule ! Validate(offer)

      expectMsg(ValidationFailed(offer, Set(ValidationFailure("Product name required")))
    }
  }

  def DummyOffer(productName:String) = {
    val url = new URL("http://www.test.com")
    val random = Random.nextInt(10)
    val randomizedProductName = if(random%2==0) "" else productName
    val randomizedCondition = if(random%8==0) "" else "new"
    val maxPrice = 100
    val price = BigDecimal(maxPrice * Random.nextDouble()).setScale(2, BigDecimal.RoundingMode.FLOOR).toDouble

    val additionalAttributes = new HashMap[String, Any]
    val product = new Product("Nike", randomizedProductName, "desc", additionalAttributes)
    val listing = new Listing(url, product, randomizedCondition)

    new Offer(java.util.UUID.randomUUID, listing, price, new Date())
  }
}
