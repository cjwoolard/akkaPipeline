import akka.actor.Actor
import com.mongodb.casbah.Imports._

case class Persist(offer:Offer)
case class OfferFound(offer:Offer)

class Persister extends Actor {
  var client = MongoClient()
  override def preStart(): Unit = {
    val settings = Settings(context.system)
    client = MongoClient(settings.Mongo.Host, settings.Mongo.Port)
  }

  def receive = {
    case Persist(offer) ⇒ {
      println("Persisting " + offer.listing.url)
      val db = client("Acquisition")
      val offers = db("offers")

      val toPersist = MongoDBObject("url" -> offer.listing.url.toString())
      offers.insert(toPersist)
    }
  }
}

class ExampleSubscriber extends Actor{

  override def preStart(): Unit = {
    context.system.eventStream.subscribe(context.self, classOf[OfferFound])
  }

  def receive = {
    case OfferFound(offer) => {
      println("Offer found:" + offer.listing.product.name + " at " + offer.price)
    }
  }
}