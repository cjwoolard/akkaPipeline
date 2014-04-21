import akka.actor.Actor
import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._

case class PersistOffer(offer:Offer){}
case class PersistQuarantinedOffer(offer:QuarantinedOffer)

case class OfferFound(offer:Offer)

class Persister extends Actor {
  var client = MongoClient()
  val clientName = "Acquisition"

  override def preStart(): Unit = {
    val settings = Settings(context.system)
    client = MongoClient(settings.Mongo.Host, settings.Mongo.Port)
  }

  def receive = {
    case PersistOffer(offer) ⇒ SaveOffer(offer)
    case PersistQuarantinedOffer(quarantinedOffer) ⇒ SaveQuarantinedOffer(quarantinedOffer)
  }

  def SaveOffer(offer:Offer){
    val db = client(clientName)
    val offers = db("CollectedOffers")
    val dbo = grater[Offer].asDBObject(offer)
    offers.insert(dbo)
  }

  def SaveQuarantinedOffer(quarantinedOffer:QuarantinedOffer){
    val db = client(clientName)
    val offers = db("QuarantinedOffers")
    val dbo = grater[QuarantinedOffer].asDBObject(quarantinedOffer)
    offers.insert(dbo)
  }
}