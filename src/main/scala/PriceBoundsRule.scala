import akka.actor.Actor

class PriceBoundsRule extends Actor{

  def receive = {
    case Validate(offer) => {
      println("Validating Price: " + offer.price)
      if(offer.price>80) {
        println("*************Failed Price Bounds Rule*****************")
        println(offer.listing.product.name + " is " + offer.price)
      }
    }
  }
}