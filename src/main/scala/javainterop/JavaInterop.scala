package javainterop

import java.io.IOException


import scala.beans.{BooleanBeanProperty, BeanProperty}

/**
  * Created by james on 5/20/2016.
  */
object JavaInterop {
  /**
    * An example Scala class
    */
  class SimpleClass(name: String, val acc: String, @BeanProperty var mutable: String) {
    //can access from Java via the method foo()
    val foo = "foo"
    //get a method _$eq. e.g. bar$_eq("newbar")
    var bar = "bar"

    //gives us getters(val/var) and setters(var)
    @BeanProperty
    val fooBean = "foobean"
    @BeanProperty
    var barBean = "barbean"
    @BooleanBeanProperty
    var awesome = true

    //Scala doesn't have checked exceptions. From Java, you can try/catch these
    //exception is erased here. have to catch Throwable from java
    def dangerFoo() = {
      throw new IOException("SURPRISE!")
    }

    //annotation lets us catch the IOException in Java-land
    @throws(classOf[IOException])
    def dangerBar() = {
      throw new IOException("NO SURPRISE!")
    }
  }

  /**
    * Traits -- interface + implementation
    * Scala compiles this to an interface called MyTrait and a companion impl class
    *   called MyTrait$class. All of the methods on the impl are static
    *
    * In Java, to call the implementation, you can just delegate the call. e.g.
    *   public String upperTraitName(){
    *     return MyTrait$class.upperTraitName(this)
    *   }
    *
    *   or, we can override it.
    */
  trait MyTrait {
    def traitName:String
    def upperTraitName = traitName.toUpperCase
  }

  /**
    * Closures Functions -- In Scala, functions are 1st class citizens
    *  “f: => T” translates to “Function0”, and “f: String => T” translates to “Function1”.
    *  Scala defines Function0 through Function22
    */
  class ClosureClass {
    def printResult[T](f: => T) = {
      println(f)
    }

    def printResult[T](f: String => T) = {
      println(f("HI THERE"))
    }
  }

  //Scala calls
  val cc = new ClosureClass
  cc.printResult { "HI MOM" }

  //To call from Java, it's not that bad. Scala provides an AbstractFunction0 and 1
  //  @Test public void closureTest() {
  //    ClosureClass c = new ClosureClass();
  //    c.printResult(new AbstractFunction0() {
  //      public String apply() {
  //        return "foo";
  //      }
  //    });
  //    c.printResult(new AbstractFunction1<String, String>() {
  //      public String apply(String arg) {
  //        return arg + "foo";
  //      }
  //    });
  //  }
  // It's ugly, but it works

}
