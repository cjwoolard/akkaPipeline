import java.net.URL
import org.scalatest._
import scala.collection.immutable.HashMap
import scalaz.{Failure, Success}

class ListingValidationSpec extends WordSpecLike with ShouldMatchers {

  val validation = new ListingValidation { override def KnownCategoriesFor(listing:Listing) = Set("known1", "known2")}

  "A listing with all known categories should pass validation" in {
    val product = new Product("","","",new HashMap[String,String])
    val listing = new Listing(new URL("http://www.test.com"), product, "", "", Set("known1", "known2"))
    val result = validation.ValidateCategories(listing)
    result should equal (Success(listing))
  }

  "A listing with unknown categories should fail validation" in {
    val product = new Product("","","",new HashMap[String,String])
    val listing = new Listing(new URL("http://www.test.com"), product, "", "", Set("known1", "known2", "unknown1", "unknown2"))
    val result = validation.ValidateCategories(listing)
    result should equal (Failure("Unknown categories unknown1,unknown2"))
  }
}