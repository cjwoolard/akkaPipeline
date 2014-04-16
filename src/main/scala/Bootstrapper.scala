import akka.actor._

object Bootstrapper extends App {

  println("System starting...")

  val system = ActorSystem("Helios")
  val persister = system.actorOf(Props[Persister])
  val acquisition = system.actorOf(Props(new Acquisition(persister:ActorRef)))
  val rule = system.actorOf(Props[ExampleSubscriber])

  acquisition ! GetOffers("Mayb")

  system.awaitTermination()
}
