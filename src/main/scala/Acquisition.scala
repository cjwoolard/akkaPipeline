import akka.actor.{ActorRef, Actor, Props}
import akka.contrib._

import akka.contrib.throttle.Throttler.{Rate, SetTarget}
import akka.contrib.throttle.TimerBasedThrottler
import java.net.URL
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

case class GetOffers(client:String){}

class Acquisition(persister:ActorRef) extends Actor {

  def receive = {
    case x: GetOffers => {
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
        val bot = context.actorOf(Props(new Bot(hostName)))
        val rate = new Rate(1, Duration(5, TimeUnit.SECONDS))
        val throttler = context.actorOf(Props(new TimerBasedThrottler(rate)), hostName)
        throttler ! SetTarget(Some(bot))
        throttler
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