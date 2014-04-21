import akka.actor.{ActorLogging, Actor}
import java.net.URL

case class Crawl(url:URL){}

class Bot(hostName:String) extends Actor with ActorLogging {

  override def preStart(){
    log.debug(s"Bot starting $hostName")
  }

  def receive = {
    case Crawl(url) ⇒ {
      log.debug("Crawling " + url)
      val offer = Offer.DummyOffer(url, "SomeProduct")
      context.parent ! OfferFound(offer)
    }
  }
}
