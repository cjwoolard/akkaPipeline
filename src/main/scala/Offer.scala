import java.net.URL
import java.util.{UUID, Date}
import scala.collection.immutable.HashMap
import scala.util.Random

case class Offer(id:UUID, listing:Listing, price:Double, time: Date)

object Offer
{
  def DummyOffer(url:URL, productName:String) = {
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
