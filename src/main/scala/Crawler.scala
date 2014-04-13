import java.util.Date
import scala.collection.immutable.HashMap

/**
 * Created by cwoolard on 4/9/2014.
 */

class Crawler
{
  def GetResults(numberOfResults:Int) : List[Offer] = {

    val results = (1 to numberOfResults).toList
    results.map(x=>{
      val additionalAttributes = new HashMap[String, Any]
      val brand = new Brand("Nike")
      val product = new Product(brand, "product"+x, "desc", additionalAttributes)
      val listing = new Listing("http://www.url.com", product, New)
      val offer = new Offer(listing, 3, new Date(2012,1,2))

      offer
    })
  }
}


