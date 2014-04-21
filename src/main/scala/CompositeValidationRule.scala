import akka.actor.{ActorRef, ActorLogging, Actor}

trait CompositeValidationRule[T] extends Actor with ActorLogging {

  val rules:Set[ActorRef]

  def receive = {
    case Validate(value:T) =>{
      context.become(runRules(value, rules, Set.empty[ValidationFailure]))
      for(rule <- rules) {
        rule ! Validate(value)
      }
    }
    case _ => {
      log.warning("Unexpected message")
    }
  }

  def runRules(value:T, rulesRemaining:Set[ActorRef], validationErrors:Set[ValidationFailure]): Receive = {
    case ValidationPassed(valuePassed:T) => {
      context.become(runRules(valuePassed, rulesRemaining - sender, validationErrors))
      CheckAllRulesCompleted(valuePassed, rulesRemaining - sender, validationErrors)
    }
    case ValidationFailed(valuePassed:T, failures) => {
      context.become(runRules(valuePassed, rulesRemaining - sender, validationErrors ++ failures))
      CheckAllRulesCompleted(valuePassed, rulesRemaining - sender, validationErrors)
    }case _ => {
      log.warning("Unexpected message")
    }
  }

  def CheckAllRulesCompleted(value:T, rulesRemaining:Set[ActorRef], validationErrors:Set[ValidationFailure]) = {
    if(rulesRemaining.isEmpty) {
      context.parent ! (if(validationErrors.isEmpty) ValidationPassed(value) else new ValidationFailed(value, validationErrors))
      context.stop(self)
    }
  }
}