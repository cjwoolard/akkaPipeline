import java.net.URL
import java.util.Date
import scala.collection.immutable.HashMap
import scala.util.Random

case class Offer(listing:Listing, price:Double, time: Date)

object Offer
{
  def DummyOffer(url:URL, productName:String) = {
    val additionalAttributes = new HashMap[String, Any]
    val product = new Product("Nike", productName, "desc", additionalAttributes)
    val listing = new Listing(url, product, New)
    val maxPrice = 100
    val price = BigDecimal(maxPrice * Random.nextDouble()).setScale(2, BigDecimal.RoundingMode.FLOOR).toDouble

    new Offer(listing, price, new Date())
  }
}
