import scalaz._
import Scalaz._

trait FormatValidation{

  def Validate[T](value:Option[String], pattern:String, success:T, failure:String) = {
    value match {
      case Some(asin)=> {
        pattern.r.findFirstIn(asin) match {
          case Some(_) => success.success
          case _ => failure.fail
        }
      }
      case _ => success.success
    }
  }
}

class ProductValidation extends SetValidation with FormatValidation {

  def ValidateProductName(product:Product) :Validation[String, Product] = {
    if(!product.name.isEmpty) product.success else "Product name required".fail
  }

  def ValidateBrand(product:Product) :Validation[String, Product] = {
    if(IsInSet(product.brand, KnownBrands)) product.success else s"Unknown brand ${product.brand}".fail
  }

  def ValidateASIN(product:Product) :Validation[String, Product] = {
    Validate(product.Asin, """([a-zA-Z0-9]{10})""", product, "Invalid ASIN")
  }

  def ValidateISBN(product:Product) :Validation[String, Product] = {
    val pattern = """ISBN(-1(?:(0)|3))?:?\x20(\s)*[0-9]+[- ][0-9]+[- ][0-9]+[- ][0-9]*[- ]*[xX0-9]"""
    Validate(product.Isbn, pattern, product, "Invalid ISBN")
  }

  def ValidateUPC(product:Product) :Validation[String, Product] = {
    product.Upc match {
      case None=> product.success
      case Some(upc) => {
        val sum = (0 /: upc.map(x=>x.asDigit).view.zipWithIndex) { case (acc,(value,index))=> if(index%2==0) acc+value else acc+(value*3) }
        val expectedCheckDigit = 10-(sum%10)%10
        val checkDigit = upc.last.asDigit
        if(checkDigit==expectedCheckDigit) product.success else "Invalid UPC".fail
      }
    }
  }

  def KnownBrands:Set[String] = Set()
}
