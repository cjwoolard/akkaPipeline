import akka.actor.Actor

class Bot(hostName:String) extends Actor{

  override def preStart(){
    println("Created new Bot for " + hostName)
  }

  def receive = {
    case Crawl(url) ⇒ {
      println("Crawling " + url)
      val offer = Offer.DummyOffer("SomeProduct")
      context.parent ! OfferFound(offer)
    }
  }
}
