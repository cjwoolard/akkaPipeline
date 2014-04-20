import com.mongodb.casbah.commons.conversions.MongoConversionHelper
import org.bson.{ BSON, Transformer }

object RegisterURLConversionHelpers extends URLHelpers {
  def apply(): Unit = {
    super.register()
  }
}

object UnregisterURLConversionHelpers extends URLHelpers {
  def apply() = {
    super.unregister()
  }
}

trait URLHelpers extends URLSerializer with URLDeserializer

trait URLSerializer extends MongoConversionHelper {

  private val encodeType = classOf[java.net.URL]

  private val transformer = new Transformer {

    def transform(o: AnyRef): AnyRef = o match {
      case url: java.net.URL => "URL~%s".format(url.toString)
      case _ => o
    }
  }

  override def register() = {
    BSON.addEncodingHook(encodeType, transformer)
    super.register()
  }

  override def unregister() = {
    BSON.removeEncodingHooks(encodeType)
    super.unregister()
  }
}

trait URLDeserializer extends MongoConversionHelper {

  private val encodeType = classOf[String]

  private val transformer = new Transformer with com.mongodb.casbah.commons.Logging {
    def transform(o: AnyRef): AnyRef = o match {
      case s: String if s.startsWith("URL~") && s.split("~").size == 2 => {
        new java.net.URL(s.split("~")(1))
      }
      case _ => o
    }
  }

  override def register() = {
    BSON.addDecodingHook(encodeType, transformer)
    super.register()
  }

  override def unregister() = {
    BSON.removeDecodingHooks(encodeType)
    super.unregister()
  }
}