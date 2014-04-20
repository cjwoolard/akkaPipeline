import akka.actor.{ExtensionIdProvider, ExtendedActorSystem, ExtensionId, Extension}
import com.typesafe.config.Config
import scala.collection.JavaConversions._

class ConfigurationSettings(configuration:Config) extends Extension {

  def NumberOfUrlsPerBot = configuration.getInt("ciq.acquisition.numberOfUrlsPerBot")

  object Mongo
  {
    val Port = configuration.getInt("ciq.mongo.port")
    val Host = configuration.getString("ciq.mongo.host")
  }

  def Bots(name:String) = {
      val bots = configuration.getConfigList("ciq.bots").map(x=>new BotConfiguration(x))
      bots.find(x=>x.Name.compareToIgnoreCase(name)==0)
  }

  class BotConfiguration(configuration:Config) {
    val Name = configuration.getString("name")
    val NumberOfMessages = configuration.getInt("numberOfMessages")
    val NumberOfSeconds = configuration.getInt("numberOfSeconds")
  }
}

object Settings extends ExtensionId[ConfigurationSettings] with ExtensionIdProvider {
  override def lookup = Settings
  override def createExtension(system:ExtendedActorSystem) = new ConfigurationSettings(system.settings.config)
}
