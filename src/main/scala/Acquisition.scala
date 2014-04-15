import akka.actor.{ActorRef, Actor, Props}
import java.net.URL

case class Crawl(url:URL){}
case class GetOffers(client:String){}

class Acquisition(persister:ActorRef) extends Actor {

  def receive = {
    case x: GetOffers => {
      println("Getting Offers")
      val urls = GetUrlsFor(x.client)
      urls.foreach(url => {
        val hostName = url.getHost
        val bot = GetOrCreateBot(hostName)
        bot ! Crawl(url)
      })
    }
    case offerFound: OfferFound => {
      context.system.eventStream.publish(offerFound)
      persister ! Persist(offerFound.offer)
    }
  }

  def GetOrCreateBot(hostName:String) : ActorRef = {
    context.child(hostName) match {
      case Some(existing) => {
        existing
      }
      case _ => {
        context.actorOf(Props(new Bot(hostName)), hostName)
      }
    }
  }

  def GetUrlsFor(client: String): IndexedSeq[URL] = {

    val cvsUrls = (1 to 10).map(x => new URL("http://www.cvs.com/" + x))

    val targetUrls = (1 to 10).map(x => new URL("http://www.target.com/" + x))

    val walmartUrls = (1 to 10).map(x => new URL("http://www.walmart.com/" + x))

    cvsUrls ++ targetUrls ++ walmartUrls
  }
}

class TargetBot extends Actor{
  def receive = {
    case _ => println("Target actor")
  }
}
