import akka.actor.{ActorRef, Actor, Props}
import akka.contrib._

import akka.contrib.throttle.Throttler.{Rate, SetTarget}
import akka.contrib.throttle.TimerBasedThrottler
import java.net.URL
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

case class GetOffers(client:String){}

class Acquisition(persister:ActorRef, dataValidation:ActorRef) extends Actor {

  val settings = Settings(context.system)

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
      persister ! Persist(offerFound.offer)
      dataValidation ! offerFound
      context.system.eventStream.publish(offerFound)
    }
  }

  def GetOrCreateBot(hostName:String) : ActorRef = {
    context.child(hostName) match {
      case Some(existing) => {
        existing
      }
      case _ => {
        settings.Bots(hostName) match{
          case Some(configuration) => {
            val bot = context.actorOf(Props(new Bot(hostName)))
            val duration = Duration(configuration.NumberOfSeconds, TimeUnit.SECONDS)
            val rate = new Rate(configuration.NumberOfMessages, duration)
            val throttler = context.actorOf(Props(new TimerBasedThrottler(rate)), hostName)
            println(s"Created throttled bot $hostName ($rate every $duration seconds max")
            throttler ! SetTarget(Some(bot))
            throttler
          }
          case None => {
            println(s"Created unthrottled bot $hostName")
            context.actorOf(Props(new Bot(hostName)), hostName)
          }
        }
      }
    }
  }

  def GetUrlsFor(client: String): IndexedSeq[URL] = {
    val numberOfUrlsPerBot = settings.NumberOfUrlsPerBot
    val cvsUrls = (1 to numberOfUrlsPerBot).map(x => new URL("http://www.cvs.com/" + x))
    val targetUrls = (1 to numberOfUrlsPerBot).map(x => new URL("http://www.target.com/" + x))
    val walmartUrls = (1 to numberOfUrlsPerBot).map(x => new URL("http://www.walmart.com/" + x))

    cvsUrls ++ targetUrls ++ walmartUrls
  }
}