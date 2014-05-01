import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import org.scalatest._

case class Catalog(id:Int, name:String)

class MongoRepository(collectionName:String) {
  val client = MongoClient("localhost", 27017)
  val db = client("Acquisition")
  val catalogs = db(collectionName)

  def GetAll() = {
    val cursor = catalogs.find()
    val results = for { x <- cursor} yield grater[Catalog].asObject(x)
    results.toList
  }

  def GetByName(name:String):Option[Catalog] = {
    val cursor = catalogs.findOne(MongoDBObject("name" -> name))
    cursor.map{catalog=>Some[Catalog](grater[Catalog].asObject(catalog))}.getOrElse(None)
  }
}

class MongoSpec extends WordSpecLike with ShouldMatchers with OptionValues with Inside {

  "Should be able to get all catalogs" in {
    val repository = new MongoRepository("Catalog")
    val catalogs = repository.GetAll()

    catalogs.length should be (6)
  }

  "Should be able to get catalog by name" in {
    val repository = new MongoRepository("Catalog")
    val catalog:Option[Catalog] = repository.GetByName("MAYB")

    inside(catalog.value) { case Catalog(id,name) =>
      id should be (5)
      name should be ("MAYB")
    }
  }
}