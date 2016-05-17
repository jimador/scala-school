package basics.continued

/**
  * Created by james on 5/17/2016.
  */
object BasicsTwo {

}

/**
  * apply methods -- for when a class has one main use
  */
class Foo {}
//I only make Foo's
class FooMaker {
  def apply() = new Foo //val foo = FooMaker()
}

class Bar {
  def apply() = 0 //val bar = new Bar
}                 //bar() -> Int = 0

/**
  * Objects -- are used to hold a single instance of a class (often for factories)
  */
object MyCounter {
  var count = 0

  def currentCount(): Long = {
    count += 1
    count
  }
}

/**
  * Classes and Objects can have the same name. The Object is called a 'Companion Object.
  * Companion objects are often used for factories too!
  */
class Baz(foo: String)
object Baz {
  //making Baz's like it's going out of style
  def apply(foo: String) = new Baz(foo)
}

/**
  * Guess what... Functions are Objects too! A Function is a set of traits.
  * More specifically, a function that takes 1 argument is an instance of a
  * Function1 trait. Not surprisingly, this trait defines the apply() method,
  * letting us call an Object like a Function. Because we all love magic numbers,
  * Scala defines Function0 through Function22
  */
object addOne extends Function1[Int, Int] {
  def apply(v1: Int): Int = v1 + 1
}

//even better as a class!
class AddOne extends (Int => Int) {
  def apply(m: Int): Int = m + 1
}

/**
  * packages let us organize our codz! If we have the colorHolder below,
  * we can reference basics.continued.colorHolder.BLUE, which seems to be
  * like a java-ish enum
  */
object colorHolder {
  val BLUE = "Blue"
  val RED = "Red"
}

/**
  * Case Classes -- used to store and match on the contents of a class.
  * They can be constructed without using 'new'
  * Equality is based on value.
  * They can have methods just like regular classes
  */
case class Calculator(brand: String, model: String) //val hp20b = Calculator("hp","20b")

/**
  * Pattern matching
  */
object matchMe {
  val times = 1

  //Matching on values
  times match {
    case 1 => "one"
    case 2 => "two"
    case _ => "some catch all"
  }

  //Matching with guards
  times match {
    case i if i == 1 => "one"
    case i if i == 2 => "two"
    case _ => "some other number"
  }

  //matching on Type
  def bigger(o: Any): Any = {
    o match {
      case i: Int if i < 0 => i - 1
      case i: Int => i + 1
      case d: Double if d < 0.0 => d - 0.1
      case d: Double => d + 0.1
      case text: String => text + "s"
    }
  }

  //Matching on class members
  def calcType(calc: Calculator) = calc match {
    case _ if calc.brand == "hp" && calc.model == "20B" => "financial"
    case _ if calc.brand == "hp" && calc.model == "48G" => "scientific"
    case _ if calc.brand == "hp" && calc.model == "30B" => "business"
    case _ => "unknown"
  }

  //other variants of matching on type
  def calcType2(calc: Calculator) = calc match {
    case Calculator("hp", "20B") => "financial"
    case Calculator("hp", "48G") => "scientific"
    case Calculator("hp", "30B") => "business"
    case Calculator(ourBrand, ourModel) => "Calculator: %s %s is of unknown type".format(ourBrand, ourModel)
  }

  def calcType3(calc: Calculator) = calc match {
    //Good matches...
    case Calculator(_, _) => "Calculator of unknown type"
  }

  def calcType4(calc: Calculator) = calc match {
    //Good matches...
    case _ => "Calculator of unknown type"
  }

  def calcType5(calc: Calculator) = calc match {
    //Good matches...
    case c@Calculator(_, _) => "Calculator: %s of unknown type".format(c)
  }
}

/**
  * Exceptions -- available via try/catch/finally that uses pattern matching
  */
object remoteCalculatorService {
  val add = addOne

  def doAdd(m: Int): Unit = {
    try {
      add(m)
    } catch {
      case e: RuntimeException => println("It's all exploding")
    } finally {
      //shut it down
    }
  }
}
