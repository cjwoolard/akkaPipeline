import akka.actor.{ActorLogging, ActorRef, Actor}

case class QuarantinedOffer(offer:Offer, validationErrors:Set[ValidationFailure])
case class Quarantine(offer:Offer, validationErrors:Set[ValidationFailure])

class DataQuarantine(persister:ActorRef) extends Actor with ActorLogging {
  def receive = {
    case Quarantine(offer, validationErrors) => {
      log.debug("Offer Quarantined " + offer + " " + validationErrors.map(x=>x.message).mkString(", "))
      persister ! PersistQuarantinedOffer(new QuarantinedOffer(offer, validationErrors))
    }
  }
}
