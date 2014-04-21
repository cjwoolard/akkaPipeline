import akka.actor.{Props, ActorRef, Actor}

class ProductNameRules extends CompositeValidationRule[Offer] {

  val rules = Set[ActorRef](
    context.actorOf(Props[ProductNameRequiredRule], "productNameRequiredRule")
  )
}

class ProductNameRequiredRule extends SimpleRule[Offer] {
  override def check(offer: Offer) = offer.listing.condition.length > 0
  override def failureMessage = "Product name required"
}