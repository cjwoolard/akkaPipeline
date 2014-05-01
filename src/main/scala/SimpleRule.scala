import akka.actor.{ActorLogging, Actor}

trait SimpleRule[TInput] extends Actor with ActorLogging {

  def check(input:TInput): Boolean
  def failureMessage: String

  def receive = {
    case Validate(value:TInput) => {
      sender ! (if(check(value))ValidationPassed(value) else ValidationFailed(value, Set(ValidationFailure(failureMessage))))
    }
    case (message:Any) => {
      log.warning(s"Unexpected message $message from $sender")
    }
  }
}