import akka.actor.{ActorRef, Actor}

class Catalog(persister:ActorRef) extends Actor {
  def receive = {
    case ValidOfferFound(offer) => persister ! Persist(offer)
  }
}
