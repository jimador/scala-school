package typesandpolymorphism

/**
  * Created by james on 5/18/2016.
  *
  * Types f: R -> N
  *
  * some features of the Scala type system
  * - Parametric Polymorphism - generics
  * - Local Type inference - val i: Int = 12
  * - Existential Quantification - defining something for an unnamed type
  * - Views - cast from one type to another
  */
object Basics {
  /**
    * Parametric Polymorphism
    * not too much difference here
    */
  def toList[A](a: A) = List(a)

  /**
    * Local Type Inference
    * Scala considers one expression at a time
    */
  def id[T](x: T) = x
  val x = id(Array(1,2,3,4))
  //x: Array[Int] = Array(1, 2, 3, 4)

  /**
    * Variance -- if T’ is a subclass of T, is Container[T’] considered a subclass of Container[T]?
    * '? extends T' and '? super T' in Java
    *
    *                  Meaning	                          Scala notation
    * covariant	    |  C[T’] is a subclass of C[T]	     |  [+T]
    * contravariant	|  C[T] is a subclass of C[T’]	     |  [-T]
    * invariant	    |  C[T] and C[T’] are not related	 |  [T]
    */

  //Example Covariant
  class Covariant[+A]

  //Generic t-> Speicalized, OK!
  val cv: Covariant[AnyRef] = new Covariant[String]

  //Specialized -> Generic, :(
  //val cvBroke: Covariant[String] = new Covariant[AnyRef]
  //error: type mismatch;
  //found   : Covariant[AnyRef]
  //required: Covariant[String]
  //val cv: Covariant[String] = new Covariant[AnyRef]

  //Example Contravariant
  class Contravariant[-A]

  //Specialized -> Generic
  val contravariant: Contravariant[String] = new Contravariant[AnyRef]

  //Generic -> Specialized
  //val contravariantBroke: Contravariant[AnyRef] = new Contravariant[String]
  //error: type mismatch;
  //found   : Contravariant[String]
  //required: Contravariant[AnyRef]
  //val fail: Contravariant[AnyRef] = new Contravariant[String]

  /*
  Note: Function parameters are Contravariant. If you need a function that takes a Bird
  and you have a function that takes an Chicken, that function would choke on a Duck.
  But a function that takes an Animal is OK
   */

  /**
    * Bounds -- restricting polymorphic variables
    */
  class Animal { val sound = "rustle" }
  //def cacophony[T](things: Seq[T]) = things.map (_.sound)
  //this doesn't work because the type system can't infer what T is and
  //if it has a 'sound' method

  //TA-DA! we just restricted T to some Type of Animal (? extends in Java)
  def biphony[T <: Animal](things: Seq[T]) = things.map (_.sound)

  class Bird extends Animal {
    override val sound: String = "tweet"
  }

  class Chicken extends Bird {
    override val sound: String = "cluck"
  }

  val flock: List[Bird] = List(new Bird, new Bird)

  //List[+T] is covariant (List of Birds is a List of Animals)
  //List defines an operator '::' that returns a new list with
  //the elem prepended
  val newFlock: List[Bird] = new Chicken :: flock

  //List also defines :: [B >: T](x: B) which returns a List[B]
  //where B is a super set of T
  val animals: List[Animal] = new Animal :: flock

  /**
    * Quantification -- because sometimes you don't give a damn
    * about types...
    *
    * Scala defines the wildcard '_' for types
    */
  //why do we care about A if all we want is the size of the List?
  //what are you, a dummy?
  def count[A](l: List[A]) = l.size

  //just give me a List, damnit! Screw yo types...
  def agnosticCount(l: List[_]) = l.size

  //Don't get too cocky, a-hole
  //agnosticCount is just short hand for 'def count(l: List[T forSome { type T }]) = l.size'
  //Now what happens when we use this?
  def drop1(l: List[_]) = l.tail
  //=> drop1: (List[_])List[Any]
  //WAT? List[Any]? All your types belong to us...
  //Type information gone... :(

  //HAVE NO FEAR! We can apply bounds to wildcards too!
  def hashcodes(l: Seq[_ <: AnyRef]) = l map (_.hashCode)
  //hashcodes: (Seq[_ <: AnyRef])Seq[Int]

  hashcodes(Seq(1,2,3))
  //error: type mismatch;
  //found   : Int(1)/
  //required: AnyRef

  /*
  Note: primitive types are not implicitly converted to AnyRef.
  You can safely force boxing by casting x.asInstanceOf[AnyRef].
  */

  hashcodes(Seq("one", "two", "three"))
  //res1: Seq[Int] = List(110182, 115276, 110339486)

}
