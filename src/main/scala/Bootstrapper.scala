import akka.actor._

object Bootstrapper extends App {

  println("System starting...")
  val system = ActorSystem("Helios")
  val persister = system.actorOf(Props[Persister])
  val acquisition = system.actorOf(Props(new Acquisition(persister:ActorRef)))
  acquisition ! GetOffers("Maybelline")

  system.awaitTermination()
}
