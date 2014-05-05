import scalaz._
import Scalaz._

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
