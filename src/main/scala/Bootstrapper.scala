import akka.actor._

object Bootstrapper extends App {

  println("System starting...")

  RegisterURLConversionHelpers()

  val system = ActorSystem("Helios")
  val persister = system.actorOf(Props[Persister])
  val dataValidation = system.actorOf(Props[DataValidation])

  val acquisition = system.actorOf(Props(new Acquisition(persister:ActorRef, dataValidation:ActorRef)), "acquisition")
  val catalog = system.actorOf(Props(new Catalog(persister:ActorRef)), "catalog")

  acquisition ! GetOffers("Mayb")

  system.awaitTermination()
}
