import akka.actor.Actor

case class QuarantinedOffer(offer:Offer, validationErrors:List[String])
case class Quarantine(offer:Offer, validationErrors:List[String])

class DataQuarantine(persister:Persister) extends Actor {
  def receive = {
    case Quarantine(offer, validationErrors) => {
      //persister ! Persist(new QuarantinedOffer(offer, validationErrors))
    }
  }
}
