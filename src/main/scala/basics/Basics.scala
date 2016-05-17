package basics

/**
  * Created by james on 5/17/2016.
  */
object Basics {
/*
  evaluations can be tested in the sbt console!
 */
  //immutable
  val two = 1 + 1
  //two = 3 <- error

  //mutable
  var name = "fred"
  name = "sue"

  //Functions - you specify types like this
  def addOne(num :Int): Int = num + 1

  //sometime functions don't need types or parans, and that's ok
  def three = 1 + 2


  //anonymous functions are cool too!
  (x: Int) => x + 1 //res6: Int => Tnt = <functional>

  //you can assign anonymous functions to vals
  val addSeven = (x: Int) => x + 7

  //it's functional, but not pure
  def timesTwoAndLog(x: Int): Int = {
    println("Logging: " + x)
    x * 2
  }

  //partial application
  def adder(m: Int, n: Int): Int = m + n

  //partially apply adder to make addTwo
  val addTwo = adder(2, _:Int)

  //curried functions -- applying some args now and others later
  def multiply(first: Int)(second: Int): Int = {
    first * second
  }

  //curry multiply to make a new function
  val timesTwo = multiply(2) _

  //we can take any function with multiple arguments and make it curried
  val curriedAdder = (adder _).curried
  val curriedAddTwo = curriedAdder(2)

  //working with variable length args
  def capitalizeAll(args :String*) = {
    args.map {
      arg => arg.capitalize
    }
  }

  def main(args: Array[String]) {
    println("Hello World!")
  }
}
//Classes
class Calculator {
  val brand: String = "HP"
  def add(m: Int, n: Int): Int = m + n
}

//Class w/ Constructor
class Calculator2(brand: String) {
  /**
    * A constructor.
    */
  val color: String = if (brand == "TI") {
    "blue"
  } else if (brand == "HP") {
    "black"
  } else {
    "white"
  }

  // An instance method.
  def add(m: Int, n: Int): Int = m + n
}

//Inheritance
class ScientificCalculator(brand: String) extends Calculator2(brand) {
  def log(m: Double, base: Double) = math.log(m) / math.log(base)
}

//Overloading
class EvenMoreScientificCalculator(brand: String) extends ScientificCalculator(brand) {
  def log(m: Int): Double = log(m, math.exp(1))
}

//Abstract classes
abstract class Shape {
  def getArea():Int //this will be defined in subclasses
}

class Circle(r: Int) extends Shape {
  override def getArea(): Int = r * r * 3
}

//Traits are collections of fields and behaviors you can extend or
//mixin to your classes

trait Car {
  val brand: String
}

trait Shiny {
  val shineRefraction: Int
}

//use extend for single trait
class BMW extends Car {
  val brand = "BMW"
}

//use the 'with' keyword for multiple traits
class NewBMW extends Car with Shiny {
  val brand = "BMW"
  val shineRefraction = 12
}

//Generic Types
trait Cache[K, V] {
  def get(key: K): V
  def put(key: K, value: V)
  def delete(key: K)
}