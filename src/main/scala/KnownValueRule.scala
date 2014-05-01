import akka.actor.{ActorLogging, Actor}

trait KnownValueRule[T] extends Actor with ActorLogging {

  def knownValues:Set[String]
  def valueToCheck(input:T): String

  def receive = {
    case Validate(value:T) => {
      val toCheck = valueToCheck(value)
      knownValues.find(x=>valueToCheck(value).compareToIgnoreCase(x)==0) match {
        case Some(knownValue:String) => sender ! ValidationPassed(value)
        case None => sender ! ValidationFailed(value, Set(ValidationFailure(s"'$toCheck' is not in expected set [" + knownValues.mkString(", ") + "]")))
      }
    }
    case (message:Any) => {
      log.warning(s"Unexpected message $message from $sender")
    }
  }
}