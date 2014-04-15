import akka.actor.Actor
import akka.actor.Actor.Receive
import com.mongodb.casbah.Imports._

/**
 * Created by cwoolard on 4/13/2014.
 */
class Persister extends Actor {
  var client = MongoClient()

  override def preStart(): Unit = {
    client = MongoClient("localhost", 27017)
  }

  def receive = {

    case Persist(offer) ⇒ {
      println("Persisting " + offer.listing.url)
      val db = client("Acquisition")
      val offers = db("offers")

      val toPersist = MongoDBObject("url" -> offer.listing.url)
      offers.insert(toPersist)
    }
  }
}

case object InitializePersistence
case class Persist(offer:Offer)
case class OfferFound(offer:Offer)

class ExampleSubscriber extends Actor{

  override def preStart(): Unit = {
    //context.system.eventStream.subscribe(context.self, classOf[OfferFound])
    println("Subscribing to OfferFound")
  }

  def receive = {
    case OfferFound(offer) => println("Offer found:" + offer.listing.product.name)
  }
}