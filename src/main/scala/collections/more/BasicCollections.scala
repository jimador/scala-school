package collections.more

/**
  * Created by james on 5/20/2016.
  */
object BasicCollections {
  //Lists
  val list = List(1, 2, 3)

  //Set
  val set = Set("a", "B", "C")

  //Sequence -- have a defined order
  val seq = Seq(1,1,2)

  //Map
  val map = Map('a' -> 1, 'b' -> 2)

  /**
    * Hierarchy -- traits that the mutable and immutable packages implement
    *
    * Traversable -- all collections can be traversed with foreach
    * Iterable -- gives you an iterator
    * Seq -- gives you a sequence with ordering
    * Set -- Collection with no dups
    * Map -- K,V
    */

  /**
    * W/ Java
    * JavaConverters package -- convert between Java and Scala collections
    *   (asScala and asJava over collections)
    *
    *
    */
}
