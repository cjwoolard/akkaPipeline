import akka.actor._

object Bootstrapper extends App {

  val system = ActorSystem("Helios")

  val acquisition = system.actorOf(Props[Acquisition])
  acquisition ! GetOffers("Maybelline")

  system.awaitTermination()
}
