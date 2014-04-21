import akka.actor.{ActorLogging, ActorRef, Actor}

case class AddToCatalog(offer:Offer)

class Catalog(persister:ActorRef) extends Actor with ActorLogging {
  def receive = {
    case AddToCatalog(offer:Offer)=> log.debug("Adding to catalog " + offer)
  }
}
