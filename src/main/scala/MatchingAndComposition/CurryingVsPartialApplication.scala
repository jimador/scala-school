package matchingandcomposition

/**
  * Created by james on 5/17/2016.
  */
object CurryingVsPartialApplication {
  /**
    * PartialFunction
    * A function works for every argument of the defined type.
    * (Int) => String takes any Int and returns a String.
    * A Partial Function is only defined for *certain values of the defined type*
    * A Partial Function (Int) => String might not accept every Int.
    */
  val one: PartialFunction[Int, String] = { case 1 => "one" }
  //one: PartialFunction[Int,String] = <function1>

  //'isDefinedAt' is a method on PartialFunction that can be used to determine
  //if the PartialFunction will accept a given argument.
  one.isDefinedAt(1)
  //res0: Boolean = true

  one.isDefinedAt(2)
  //res1: Boolean = false

  //You can apply a partial function.
  one(1)
  //res2: String = one

  //orElse reflects whether the PartialFunction is defined over the supplied argument.
  val two: PartialFunction[Int, String] = { case 2 => "two" }
  //two: PartialFunction[Int,String] = <function1>

  val three: PartialFunction[Int, String] = { case 3 => "three" }
  //three: PartialFunction[Int,String] = <function1>

  val wildcard: PartialFunction[Int, String] = { case _ => "something else" }
  //wildcard: PartialFunction[Int,String] = <function1>

  val partial = one orElse two orElse three orElse wildcard
  //partial: PartialFunction[Int,String] = <function1>

  partial(5)
  //res24: String = something else

  partial(3)
  //res25: String = three

  partial(2)
  //res26: String = two

  partial(1)
  //res27: String = one

  partial(0)
  //res28: String = something else

  //From the map example in Functional Combinators, we can derive
  //a case example
  case class PhoneExt(name: String, ext: Int)
  val extensions = List(PhoneExt("steve", 100), PhoneExt("robey", 200))

  //a filter takes a function, partial function is a subtype of function.
  extensions.filter { case PhoneExt(name, extension) => extension < 200 }
  //res0: List[PhoneExt] = List(PhoneExt(steve,100))
}


