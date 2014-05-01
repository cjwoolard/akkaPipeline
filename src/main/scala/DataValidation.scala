import akka.actor.{ActorLogging, Props, ActorRef, Actor}

case class Validate[T](value:T)
case class ValidOfferFound(offer:Offer)
case class InvalidOfferFound(offer:Offer, failedRules:Set[ValidationFailure])
case class ValidationPassed[T](value:T)
case class ValidationFailed[T](value:T, failedRules:Set[ValidationFailure])
case class ValidationFailure(message:String)

class DataValidation(catalog:ActorRef, quarantine:ActorRef) extends Actor with ActorLogging {

  val eventStream = context.system.eventStream

  override def preStart = {
    eventStream.subscribe(self, classOf[OfferFound])
  }

  def receive = {
    case OfferFound(offer) => {
       val validator = context.actorOf(Props[OfferRules], "offerRules" + offer.id)
      validator ! Validate(offer)
    }
    case ValidationPassed(offer:Offer) => {
      //log.debug("Valid Offer Found " + offer)
      catalog ! AddToCatalog(offer)
      eventStream.publish(new ValidOfferFound(offer))
    }
    case ValidationFailed(offer:Offer, failures) => {
      //log.debug("Invalid Offer Found  " + failures.map(x=>x.message).mkString(", ") + " " + offer)
      quarantine ! Quarantine(offer, failures)
      eventStream.publish(new InvalidOfferFound(offer, failures))
    }
    case (message:Any) => {
      log.warning(s"Unexpected message $message from $sender")
    }
  }
}



