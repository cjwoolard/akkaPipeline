import java.net.URL

case class Listing(url:URL, product:Product, sellerName:String, condition:String, categories:Set[String])
