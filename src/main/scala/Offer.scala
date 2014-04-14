import java.util.Date
import scala.collection.immutable.HashMap

/**
 * Created by cwoolard on 4/9/2014.
 */
case class Offer(listing:Listing, price:Int, time: Date) {

}

object Offer
{
  def DummyOffer(productName:String) = {
    val additionalAttributes = new HashMap[String, Any]
    val brand = new Brand("Nike")
    val product = new Product(brand, productName, "desc", additionalAttributes)
    val listing = new Listing("http://www.url.com", product, New)

    new Offer(listing, 3, new Date(2012,1,2))
  }
}
