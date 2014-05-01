import akka.actor.{ActorRef, ActorLogging, Actor}

trait CompositeValidationRule[T] extends Actor with ActorLogging {

  val rules:Set[ActorRef]

  def receive = {
    case Validate(value:T) =>{
      //println("Validating: " + value)
//      context.become(runRules(sender, value, rules, Set.empty[ValidationFailure]))
      context.become(waitForRulesToComplete2)
      //for(rule <- rules) rule ! Validate(value)
    }
    case (message:Any) => {
      log.warning("Unexpected message")
    }
  }

  def waitForRulesToComplete2 : Receive =
  {
    case (message:Any) => {
      log.warning("Unexpected message")
    }
  }

  def waitForRulesToComplete(requestor:ActorRef) : Receive =
  {
//    case ValidationPassed(valuePassed:T) => {
//      println("Rule Passed: " + sender.path)
//      context.become(waitForRulesToComplete(requestor)
//      //CheckAllRulesCompleted(requestor, valuePassed, rulesRemaining - sender, validationErrors)
//    }
//    case ValidationFailed(valuePassed:T, failures) => {
//      println("Rule Failed: " + sender.path)
//      context.become(waitForRulesToComplete(requestor, count+1))
//      //CheckAllRulesCompleted(requestor, valuePassed, rulesRemaining - sender, validationErrors ++ failures)
//    }
    case (message:Any) => {
      log.warning("Unexpected message")
    }
  }

  def runRules(requestor:ActorRef, value:T, rulesRemaining:Set[ActorRef], validationErrors:Set[ValidationFailure]): Receive = {
    case ValidationPassed(valuePassed:T) => {
      context.become(runRules(requestor, valuePassed, rulesRemaining - sender, validationErrors))
      CheckAllRulesCompleted(requestor, valuePassed, rulesRemaining - sender, validationErrors)
    }
    case ValidationFailed(valuePassed:T, failures) => {
      context.become(runRules(requestor, valuePassed, rulesRemaining - sender, validationErrors ++ failures))
      CheckAllRulesCompleted(requestor, valuePassed, rulesRemaining - sender, validationErrors ++ failures)
    }
    case (message:Any) => {
      log.warning(s"Unexpected message $message from $sender")
    }
  }

  def CheckAllRulesCompleted(requestor:ActorRef, value:T, rulesRemaining:Set[ActorRef], validationErrors:Set[ValidationFailure]) = {
    if(rulesRemaining.isEmpty) {
      requestor ! (if(validationErrors.isEmpty) ValidationPassed(value) else new ValidationFailed(value, validationErrors))
      context.stop(self)
    }
  }
}