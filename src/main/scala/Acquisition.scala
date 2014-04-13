import akka.actor.{ActorRef, Actor, Props}
import java.net.URL

class Acquisition extends Actor {
  override def preStart(): Unit = {

    //val greeter = context.actorOf(Props[Greeter], "greeter")
    //    greeter ! Greeter.Greet
  }

  def receive = {
    case x: GetOffers => {
      val urls = GetUrlsFor(x.client)
      urls.foreach(url => {
        val hostName = url.getHost
        val bot = GetOrCreateBot(hostName)
        bot ! Crawl(url)
      })
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

case class GetOffers(client:String){}
case class Crawl(url:URL){}

class Bot(hostName:String) extends Actor{

  override def preStart(){
      println("Created new Bot for " + hostName)
  }

  def receive = {
    case Crawl(url) ⇒ {
      println("Crawling " + url)
    }
  }
}

class TargetBot extends Actor{
  def receive = {
    case _ => println("Target actor")
  }
}