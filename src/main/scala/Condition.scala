/**
 * Created by cwoolard on 4/9/2014.
 */
sealed trait Condition { def name:String }
case object New extends Condition { val name = "New" }
case object Used extends Condition { val name = "Used" }
case class Other(name: String) extends Condition

