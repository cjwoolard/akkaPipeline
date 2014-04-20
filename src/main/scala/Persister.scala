import akka.actor.Actor
import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._

case class Persist(offer:Offer)
case class OfferFound(offer:Offer)

class Persister extends Actor {
  var client = MongoClient()

  override def preStart(): Unit = {
    val settings = Settings(context.system)
    client = MongoClient(settings.Mongo.Host, settings.Mongo.Port)
  }

  def receive = {
    case Persist(offer) ⇒ SaveOffer(offer)
  }

  def SaveOffer(offer:Offer){
    val db = client("Acquisition")
    val offers = db("offers")
    val dbo = grater[Offer].asDBObject(offer)
    offers.insert(dbo)
  }

  def Save(offer:Offer){
    val db = client("Acquisition")
    val offers = db("offers")
    val dbo = grater[Offer].asDBObject(offer)
    offers.insert(dbo)
  }
}