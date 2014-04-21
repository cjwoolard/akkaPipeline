import akka.actor.{Props, ActorRef}

class ConditionRules extends CompositeValidationRule[Offer] {

  val rules = Set[ActorRef](
    context.actorOf(Props[ConditionRequiredRule], "conditionKnownValuesRule"),
    context.actorOf(Props[ConditionKnownValueRule], "conditionRequiredRule")
  )
}

class ConditionKnownValueRule extends KnownValueRule[Offer]
{
  override def knownValues = Set("New","Used")
  override def valueToCheck(input:Offer) = input.listing.condition
}

class ConditionRequiredRule extends SimpleRule[Offer] {
  override def check(offer: Offer) = offer.listing.condition.length > 0
  override def failureMessage = "Condition required"
}