import akka.actor.Actor

class ProductNameRule  extends Actor{

  def receive = {
    case RunValidationRule(offer) => {
      println("Validating Product Name: " + offer.listing.product.name)
      if(offer.listing.product.name.length==0) {
        println("*************Failed Product Name Rule*****************")
      }
    }
  }
}