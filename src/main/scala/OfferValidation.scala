import scalaz._
import Scalaz._

trait SetValidation{
  def IsInSet(value:String, knownValues:Set[String]) = {
    knownValues.find(x=>value.compareToIgnoreCase(x)==0) match {
      case Some(_) => true
      case None => false
    }
  }
}

class OfferValidation extends SetValidation {

  def ValidatePrice(offer: Offer): Validation[String, Offer] = {
    if (offer.price<=0) "Price must be positive".fail else offer.success
  }

  def ValidateShippingCost(offer: Offer): Validation[String, Offer] = {
    if (offer.shippingCost<=0) "Price must be positive".fail else offer.success
  }
}