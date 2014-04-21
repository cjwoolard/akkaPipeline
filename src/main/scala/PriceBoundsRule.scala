
class PriceBoundsRule extends SimpleRule[Offer] {
  override def check(offer: Offer) = offer.price > 0 && offer.price < 80
  override def failureMessage = "Offer price must be within bounds"
}