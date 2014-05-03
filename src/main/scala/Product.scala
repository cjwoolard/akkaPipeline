import scala.collection.immutable.HashMap

case class Product(brand:String, name:String, description:String, additionalAttributes:HashMap[String, String]){
  val Asin = additionalAttributes.get("asin")
  val Upc = additionalAttributes.get("upc")
  val Ean = additionalAttributes.get("ean")
  val Jan = additionalAttributes.get("jan")
  val Mpn = additionalAttributes.get("mpn")
}
