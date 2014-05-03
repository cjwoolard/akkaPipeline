import java.net.URL
import org.scalatest._
import scala.collection.immutable.HashMap
import scalaz.{Failure, Success}

class ProductValidationSpec extends WordSpecLike with ShouldMatchers {
  val validation = new ProductValidation()

  "A product with a name should pass validation" in {
    val product = new Product("", "someName", "", new HashMap[String, String])
    val result = validation.ValidateProductName(product)
    result should equal(Success(product))
  }

  "A product with no name should fail validation" in {
    val product = new Product("", "", "", new HashMap[String, String])
    val result = validation.ValidateProductName(product)
    result should equal(Failure("Product name required"))
  }

   "A product with no ASIN should pass validation" in {
    val product = new Product("", "", "", new HashMap[String, String])
    val result = validation.ValidateASIN(product)
    result should equal(Success(product))
  }

  "A product with a valid ASIN should pass validation" in {
    val product = new Product("", "", "", HashMap[String, String]("asin"->"1234567890"))
    val result = validation.ValidateASIN(product)
    result should equal(Success(product))
  }

  "A product with an invalid ASIN should fail validation" in {
    val product = new Product("", "", "", HashMap[String, String]("asin"->"12345"))
    val result = validation.ValidateASIN(product)
    result should equal(Failure("Invalid ASIN"))
  }
}