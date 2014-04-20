import akka.actor.{Props, ActorRef, Actor}

case class Validate(offer:Offer, rules:Set[ActorRef])
case class ValidOfferFound(offer:Offer)
case class InvalidOfferFound(offer:Offer, failedRules:Set[String])
case class ValidationRulePassed(rule:ActorRef)
case class ValidationRuleFailed(rule:ActorRef, failure:ValidationError)
case class ValidationError(message:String)

class DataValidation extends Actor {

  val eventStream = context.system.eventStream

  val rules = Set[ActorRef](
    context.actorOf(Props[PriceBoundsRule], "priceBoundsRule"),
    context.actorOf(Props[ProductNameRule], "productNameRule"),
    context.actorOf(Props[ConditionRule], "conditionRule")
  )

  def receive = {
    case OfferFound(offerFound) => {
      val validator = context.actorOf(Props[Validator])
      validator ! Validate(offerFound, rules)
    }
    case ValidOfferFound(offer) => eventStream.publish(new ValidOfferFound(offer))
    case InvalidOfferFound(offer, failures) => eventStream.publish(new InvalidOfferFound(offer, failures))
  }
}

class Validator() extends Actor {

  def receive = {
    case Validate(offer, rules) =>{
      context.become(runRules(offer, Set.empty[ActorRef], Set.empty[ValidationError]))
    }
  }

  def runRules(offer:Offer, rules:Set[ActorRef], validationErrors:Set[ValidationError]): Receive = {
    case ValidationRulePassed(rule) => {
      context.become(runRules(offer, rules-rule, validationErrors))
      CheckAllRulesCompleted(offer, rules, validationErrors)
    }
    case ValidationRuleFailed(rule, failure:ValidationError) => {
      context.become(runRules(offer, rules-rule, validationErrors+failure))
      CheckAllRulesCompleted(offer, rules, validationErrors)
    }
  }

  def CheckAllRulesCompleted(offer:Offer, rules:Set[ActorRef], validationErrors:Set[ValidationError]) = {
    val message = rules.isEmpty match {
      case true =>  new ValidOfferFound(offer)
      case false => new InvalidOfferFound(offer, validationErrors.map(x=>x.message))
    }
    context.parent ! message
    context.stop(self)
  }
}

