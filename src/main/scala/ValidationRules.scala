import scalaz._
import Scalaz._
import scala.util.matching._

trait SetValidation{
  def IsInSet(value:String, knownValues:Set[String]) = {
    knownValues.find(x=>value.compareToIgnoreCase(x)==0) match {
      case Some(_) => true
      case None => false
    }
  }
}

class ProductValidation extends SetValidation {

  def ValidateProductName(product:Product) :Validation[String, Product] = {
    if(product.name.isEmpty) "Product name required".fail else product.success
  }

  def ValidateBrand(product:Product) :Validation[String, Product] = {
    if(IsInSet(product.brand, KnownBrands)) "Brand unknown".fail else product.success
  }

  def ValidateASIN(product:Product) :Validation[String, Product] = {

    product.Asin match {
      case Some(asin)=> {
           """([a-zA-Z0-9]{10})""".r.findFirstIn(asin) match {
           case Some(_) => product.success
           case _ => "Invalid ASIN".fail
         }
      }
      case _ => product.success
    }
  }

  def KnownBrands:Set[String] = Set()
}

class ListingValidation extends SetValidation {

  def ValidateCondition(listing:Listing) :Validation[String, Listing] = {
    if(IsInSet(listing.condition, KnownConditions)) "Condition unknown".fail else listing.success
  }

  def ValidateRetailerName(listing:Listing) :Validation[String, Listing] = {
    if(listing.sellerName.isEmpty) "Retailer name required".fail else listing.success
  }

  def ValidateCategories(listing:Listing) :Validation[String, Listing] = {
    val unknownCategories = listing.categories.diff(KnownCategoriesFor(listing))
    if(unknownCategories.isEmpty) listing.success else s"Unknown categories ${unknownCategories.mkString(",")}".fail
  }

  def KnownConditions:Set[String] = Set()

  def KnownCategoriesFor(listing:Listing) :Set[String] = Set()
}


class OfferValidation extends SetValidation {

  def ValidatePrice(offer: Offer): Validation[String, Offer] = {
    if (offer.price<=0) "Price must be positive".fail else offer.success
  }

  def ValidateShippingCost(offer: Offer): Validation[String, Offer] = {
    if (offer.shippingCost<=0) "Price must be positive".fail else offer.success
  }
}