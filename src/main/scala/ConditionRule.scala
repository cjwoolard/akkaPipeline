import akka.actor.Actor

class ConditionRule extends Actor{

  def receive = {
    case RunValidationRule(offer) => {
      println("Validating Condition: " + offer.listing.condition)
      if(offer.listing.condition!=Used) {
        println("*************Failed Condition Rule*****************")
        println("Condition is " + offer.listing.condition)
      }
    }
  }
}