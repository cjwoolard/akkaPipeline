import org.scalatest._
import scala.collection.immutable.HashMap
import scalaz.{Failure, Success}

class ProductValidationSpec extends FreeSpecLike with ShouldMatchers {

  val validation = new ProductValidation()

  "Name validation" - {
    "A product with a name should pass" - {
      val product = new Product("", "someName", "", new HashMap[String, String])
      val result = validation.ValidateProductName(product)
      result should equal(Success(product))
    }

    "A product with no name should fail" - {
      val product = new Product("", "", "", new HashMap[String, String])
      val result = validation.ValidateProductName(product)
      result should equal(Failure("Product name required"))
    }
  }

  "Brand validation" - {

    val validation = new ProductValidation() { override def KnownBrands = { Set("knownBrand") }}

    "A product with a recognized brand should pass" - {
      val product = new Product("knownBrand", "", "", new HashMap[String, String])
      val result = validation.ValidateBrand(product)
      result should equal(Success(product))
    }

    "A product with an unrecognized brand should fail" - {
      val product = new Product("unknownBrand", "", "", new HashMap[String, String])
      val result = validation.ValidateBrand(product)
      result should equal(Failure("Unknown brand unknownBrand"))
    }
  }

  "ASIN validation" - {

    "A product with no ASIN should pass" - {
      val product = new Product("", "", "", new HashMap[String, String])
      val result = validation.ValidateASIN(product)
      result should equal(Success(product))
    }
  
    "A product with a valid ASIN should pass" - {
      val product = new Product("", "", "", HashMap[String, String]("asin"->"1234567890"))
      val result = validation.ValidateASIN(product)
      result should equal(Success(product))
    }
  
    "A product with an invalid ASIN should fail" - {
      val product = new Product("", "", "", HashMap[String, String]("asin"->"12345"))
      val result = validation.ValidateASIN(product)
      result should equal(Failure("Invalid ASIN"))
    }
  }

  "ISBN validation" - {

    "A product with no ISBN should pass" - {
      val product = new Product("", "", "", new HashMap[String, String])
      val result = validation.ValidateISBN(product)
      result should equal(Success(product))
    }

    "A product with a valid ISBN should pass" - {
      val product = new Product("", "", "", HashMap[String, String]("isbn"->"ISBN 123-4-567-89012-3"))
      val result = validation.ValidateISBN(product)
      result should equal(Success(product))
    }

    "A product with an invalid ISBN should fail" - {
      val product = new Product("", "", "", HashMap[String, String]("isbn"->"ISBN 9781"))
      val result = validation.ValidateISBN(product)
      result should equal(Failure("Invalid ISBN"))
    }
  }


  "UPC validation" - {

    "A product with no UPC should pass" - {
      val product = new Product("", "", "", new HashMap[String, String])
      val result = validation.ValidateUPC(product)
      result should equal(Success(product))
    }

    "A product with a valid UPC should pass" - {
      val product = new Product("", "", "", HashMap[String, String]("upc"->"036000291452"))
      val result = validation.ValidateUPC(product)
      result should equal(Success(product))
    }

    "A product with an invalid UPC should fail" - {
      val product = new Product("", "", "", HashMap[String, String]("upc"->"UPC 9781"))
      val result = validation.ValidateUPC(product)
      result should equal(Failure("Invalid UPC"))
    }
  }
}