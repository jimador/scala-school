package collections

/**
  * Created by james on 5/17/2016.
  *
  * Common functions over collections in scala
  */
object FunctionalCombinators {
  val numbers = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  val letters = List("a","b","c","d")

  //map -- yep, there's a map function
  val timeTwo = numbers.map((n: Int) => n * 2)

  //foreach -- returns nothing. for side-effects only
  numbers.foreach((n: Int) => n * 3)

  //filter -- yep, there's that too
  numbers.filter((n: Int) => n % 2 == 0)

  //zip -- two into 1
  val zipped = numbers.zip(letters)

  //partition -- tuple of collections based on a predicate
  val colls = numbers.partition((n: Int) => n % 2 == 0)

  //find -- find the first element that matches the predicate returns an Option[T]
  val contains5 = numbers.find((n: Int) => n == 5)

  //drop -- drops the first n elements
  numbers.drop(5)

  //dropWhile -- remove elements until the predicate is NO LONG SATISFIED
  numbers.dropWhile(_ % 2 != 0) // 2,3,4,5,6,7,8,9,10

  //foldLeft
  numbers.foldLeft(0)((m: Int, n: Int) => m + n) // same as sum in this example = 55

  //foldRight
  numbers.foldRight(0)((m: Int, n: Int) => m + n) // same as fold left, but starts at the RHS

  //flatten -- 2 collections into 1
  val flattened = List(List(1,2), List(3,4)).flatten // (1,2,3,4)

  //flatMap -- combination of flatten and map. takes a function that works over a nested collection
  //and concatenates the results back together
  val addOneNested = List(List(1,2), List(3,4)).flatMap(x => x.map(_ + 1))

  //All of our Functional Combinators we've seen can be written on top of fold
  def ourMap(numbers: List[Int], fn: Int => Int): List[Int] = {
    numbers.foldRight(List[Int]()) { (x: Int, xs: List[Int]) =>
      fn(x) :: xs
    }
  }

  //They work on Maps too, after all, maps can be thought of as a list of pairs
  val extensions = Map("steve" -> 100, "bob" -> 101, "joe" -> 201)
  //each item returned is a tuple
  extensions.filter((namePhone: (String, Int)) => namePhone._2 < 200)
  //alternatively, PATTERN MATCHING!!!
  extensions.filter({case (name, extension) => extension < 200})
}
