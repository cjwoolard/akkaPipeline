import akka.actor.{Props, ActorRef}

class OfferRules extends CompositeValidationRule[Offer] {
  val rules = Set[ActorRef](
    context.actorOf(Props[ProductNameRules], "productNameRule"),
    context.actorOf(Props[PriceBoundsRule], "priceBoundsRule"),
    context.actorOf(Props[ConditionRules], "conditionRule")
  )
}