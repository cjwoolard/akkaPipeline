import akka.actor.Actor
import java.net.URL

case class Crawl(url:URL){}

class Bot(hostName:String) extends Actor{

  override def preStart(){
    println("Created new Bot for " + hostName)
  }

  def receive = {
    case Crawl(url) ⇒ {
      println("Crawling " + url)
      val offer = Offer.DummyOffer(url, "SomeProduct")
      context.parent ! OfferFound(offer)
    }
  }
}
