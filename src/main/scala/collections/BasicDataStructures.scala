package collections

/**
  * Created by james on 5/17/2016.
  */
object BasicDataStructures {
  //Lists
  val numbers = List(1, 2, 3, 4)

  //Sets
  val set = Set(1, 1, 2)

  //Tuple -- provides some neat features
  val hostPort = ("localhost", 80)
  hostPort._1 // String = localhost
  hostPort._2 // Int = 80

  hostPort match {
    case ("localhost", port) => //....
    case (host, port) => //...
  }

  //special scala sauce for making tuples of 2 values
  val oneTwo = 1 -> 2 // (Int,Int) = (1,2)

  //see Effective Scala for opinions about destructuring bindings in tuples

  //Maps and maps of maps of maps of maps
  val myMap = Map(1 -> Map("foo" -> "bar"))

  //Option -- just like any other Option type -> Some or None are the subclasses
  val nums = Map("one"->1, "two"->2)
  var numsAtTwo = nums.get("two") // Option[Int] = Some[2]
  var numsAtThree = nums.get("three") // Option[Int] = None

  //How do we deal with the Option type? getOrElse
  val result = nums.getOrElse("four", 0) * 2

  //Or we can use pattern matching
  val matchingResult = nums.get("one") match {
    case Some(n) => n * 2
    case None => 0
  }
}
