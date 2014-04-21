import akka.actor._

object Bootstrapper extends App {

  RegisterURLConversionHelpers()

  val system = ActorSystem("Helios")
  val persister = system.actorOf(Props[Persister], "persister")
  val acquisition = system.actorOf(Props(new Acquisition(persister:ActorRef, dataValidation:ActorRef)), "acquisition")
  val catalog = system.actorOf(Props(new Catalog(persister)), "catalog")
  val quarantine = system.actorOf(Props(new DataQuarantine(persister)), "quarantine")
  val dataValidation = system.actorOf(Props(new DataValidation(catalog, quarantine)), "dataValidation")

  Thread.sleep(1000)

  acquisition ! GetOffers("Mayb")

  system.awaitTermination()
}
