import akka.actor.{ActorLogging, Actor}

trait SimpleRule[TInput] extends Actor with ActorLogging {

  def check(input:TInput): Boolean
  def failureMessage: String

  def receive = {
    case Validate(value:TInput) => {
      context.parent ! (if(check(value))ValidationPassed(value) else ValidationFailed(value, Set(ValidationFailure(failureMessage))))
    }
    case _=> {
      log.warning("Unexpected message")
    }
  }
}