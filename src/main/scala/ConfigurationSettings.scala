import akka.actor.{ExtensionIdProvider, ExtendedActorSystem, ExtensionId, Extension}
import com.typesafe.config.Config

class ConfigurationSettings(configuration:Config) extends Extension {

  object Mongo
  {
    val Port = configuration.getInt("ciq.mongo.port")
    val Host = configuration.getString("ciq.mongo.host")
  }
}

object Settings extends ExtensionId[ConfigurationSettings] with ExtensionIdProvider
{
  override def lookup = Settings
  override def createExtension(system:ExtendedActorSystem) = new ConfigurationSettings(system.settings.config)
}