package typesandpolymorphism

import scala.concurrent.Future

/**
  * Created by james on 5/18/2016.
  * Advanced Types:
  * - View Bounds
  * - Other Type Bounds
  * - Higher Kinded Types & Ad-Hoc Polymorphism
  * - F-Bounded Polymorphism / Recursive Types
  * - Structural Types
  * - Abstract Type Members
  * - Type Erasures & Manifests
  *
  * Bonus: Case study: Finagle
  */
object Advanced {

}

object ViewsAndOtherTypeBounds {
  /**
    * View Bounds ("type classes") -- aka faking it with conversion
    * When you want a class to be 'viewed as' another (not sub/super/equal)
    * Uses: when an operation needs to "read" an object, but not modify it
    *   *can help to satisfy type inference
    */
  implicit def strToInt(x: String) = x.toInt
  //strToInt: (x: String)Int

  "123"
  //res0: java.lang.String = 123

  val y: Int = "123"
  //y: Int = 123

  math.max("123", 111)
  //res1: Int = 123

  //specify a view bound with '<%'
  class Container[A <% Int] {def addIt(x: A) = 123 + x}

  /**
    * Other Type Bounds
    * Methods can enforce more complex type bounds via implicit parameters.
    * For example, List supports sum on numeric contents but not on others.
    * Alas, Scala’s numeric types don’t all share a superclass, so we can’t just say
    * 'T <:' Number. Instead, to make this work, Scala’s math library defines an implicit
    * Numeric[T] for the appropriate types T. Then in List’s definition uses it:
    */

  /**
    * Generic Programming with Views
    *
    * In the Scala standard library, views are primarily used to implement
    * generic functions over collections. For example, the “min” function
    * (on Seq[]), uses this technique:
    *
    * def min[B >: A](implicit cmp: Ordering[B]): A = {
    * if (isEmpty)
    *   throw new UnsupportedOperationException("empty.min")
    *
    * reduceLeft((x, y) => if (cmp.lteq(x, y)) x else y)
    * }
    *
    * The main advantages of this are:
    *   Items in the collection aren't required to implement Ordered, but
    *     Ordered uses are still statically checked
    *   You can define your own orderings without any additional support
    */


  /**
    * Context Bounds and Implicity[]
    *
    * Scala 2.8 introduced a shorthand for threading through & accessing implicit arguments.
    */
  def foo[A](implicit x: Ordered[A]) {}
  def foo[A : Ordered]() {}
}

object HigherKindedTypesAndAdHocPoly {
  /**
    * Scala can abstract over “higher kinded” types. For example, suppose that you
    * needed to use several types of containers for several types of data. You might
    * define a Container interface that might be implemented by means of several
    * container types: an Option, a List, etc. You want to define an interface for
    * using values in these containers without nailing down the values’ type.
    *
    * This is analogous to function currying. For example, whereas “unary types”
    * have constructors like List[A], meaning we have to satisfy one “level” of type
    * variables in order to produce a concrete types (just like an uncurried function
    * needs to be supplied by only one argument list to be invoked), a higher-kinded
    * type needs more.
    */
  //Example Container
  //Note: that Container is polymorphic in a parametrized type (“container type”)
  trait Container[M[_]] { def put[A](x: A): M[A]; def get[A](m: M[A]): A }

  //a container with a List[A]
  val container = new Container[List] { def put[A](x: A) = List(x); def get[A](m: List[A]) = m.head }

  container.put("hey")
  //res24: List[java.lang.String] = List(hey)

  container.put(123)
  //res25: List[Int] = List(123)

  //If we combine using containers with implicits, we get “ad-hoc” polymorphism:
  //the ability to write generic functions over containers.

  //List Container
  implicit val listContainer = new Container[List] { def put[A](x: A) = List(x); def get[A](m: List[A]) = m.head }

  //Option Container
  implicit val optionContainer = new Container[Some] { def put[A](x: A) = Some(x); def get[A](m: Some[A]) = m.get }

  def tupleize[M[_]: Container, A, B](fst: M[A], snd: M[B]) = {
     val c = implicitly[Container[M]]
     c.put(c.get(fst), c.get(snd))
  }

  tupleize(Some(1), Some(2))
  //res33: Some[(Int, Int)] = Some((1,2))

  tupleize(List(1), List(2))
  //res34: List[(Int, Int)] = List((1,2))
}

object FBoundedPolymorphism {

  /**
    * Often it’s necessary to access a concrete subclass in a (generic) trait.
    * For example, imagine you had some trait that is generic, but can be
    * compared to a particular subclass of that trait.
    */
  //damn, now we need a compare
  trait Container extends Ordered[Container] {
    def compare(that: Container): Int
  }

  //but wait! I only want to compare MyContainer, boom!
  //Ordered is now paramiterized on A, which is itself a container
  class MyContainer extends Container[MyContainer] {
    def compare(that: MyContainer): Int = 1
  }

  List(new MyContainer, new MyContainer, new MyContainer)
  //res3: List[MyContainer] = List(MyContainer@30f02a6d, MyContainer@67717334, MyContainer@49428ffa)

  List(new MyContainer, new MyContainer, new MyContainer).min
  //res4: MyContainer = MyContainer@33dfeb30

  //Now that all are subtypes of Container[_]
  class YourContainer extends Container[YourContainer] {
    def compare(that: YourContainer) = 0
  }

  List(new MyContainer, new MyContainer, new MyContainer, new YourContainer)
  //res2: List[Container[_ >: YourContainer with MyContainer <: Container[_ >: YourContainer with MyContainer <: ScalaObject]]]
  //= List(MyContainer@3be5d207, MyContainer@6d3fe849, MyContainer@7eab48a7, YourContainer@1f2f0ce9)

  /*
  Note: the resulting type is now lower-bound by YourContainer with MyContainer.
  it only provides a logical greatest lower bound for the unified type of the list.
  What happens if we try to use Ordered now?
   */
  List(new MyContainer, new MyContainer, new MyContainer, new YourContainer).min
  //error: could not find implicit value for parameter cmp:
  //Ordering[Container[_ >: YourContainer with MyContainer <: Container[_ >: YourContainer with MyContainer <: ScalaObject]]]
}

object StructuralTypes {
  /**
    * Scala has support for structural types — type requirements are expressed by
    * interface structure instead of a concrete type.
    *
    * Note: This can be quite nice in many situations, but the implementation
    * uses reflection, so be performance-aware!
    */
  //notice where the second def is? yea, that's right, you like that, don't ya?
  def foo(x: { def get: Int }) = 123 + x.get
  foo(new { def get = 10 })
}

object AbstractTypeMembers {
  /**
    * In a trait, you can leave type members abstract
    */
  trait Foo { type A; val x: A; def getX: A = x }
  val fooI = new Foo {
    type A = Int
    val x = 123
  }.getX

  val fooS = new Foo {
    type A = String
    val x = "hey"
  }.getX

  //This can be usful when doing DI.
  //Refer to the abstract type member using the # operator
  trait Bar[M[_]] { type t[A] = M[A] }
  val x: Bar[List]#t[Int] = List(1)
}

object TypeErasuresAndManifests {
  /**
    * Manifests provide a way to selectivly recover type information after
    * erasure. They are provided as an implicit value, generated by the
    * compiler as needed
    */
  class MakeFoo[A](implicit manifest: Manifest[A]) {
    def make: A = manifest.erasure.newInstance.asInstanceOf[A]
  }
  val unerasedA = {
    new MakeFoo[String].make
  }
}
/*
object FinagleCaseStudy {
  trait Service[-Req, +Rep] extends (Req => Future[Rep])

  trait Filter[-ReqIn, +RepOut, +ReqOut, -RepIn]
    extends ((ReqIn, Service[ReqOut, RepIn]) => Future[RepOut])
  {
    def andThen[Req2, Rep2](next: Filter[ReqOut, RepIn, Req2, Rep2]) =
      new Filter[ReqIn, RepOut, Req2, Rep2] {
        def apply(request: ReqIn, service: Service[Req2, Rep2]) = {
          Filter.this.apply(request, new Service[ReqOut, RepIn] {
            def apply(request: ReqOut): Future[RepIn] = next(request, service)
            override def release() = service.release()
            override def isAvailable = service.isAvailable
          })
        }
      }

    def andThen(service: Service[ReqOut, RepIn]) = new Service[ReqIn, RepOut] {
      private[this] val refcounted = new RefcountedService(service)

      def apply(request: ReqIn) = Filter.this.apply(request, refcounted)
      override def release() = refcounted.release()
      override def isAvailable = refcounted.isAvailable
    }
  }

  //A service may authenticate requests with a filter.
  trait RequestWithCredentials extends Request {
    def credentials: Credentials
  }

  class CredentialsFilter(credentialsParser: CredentialsParser)
    extends Filter[Request, Response, RequestWithCredentials, Response]
  {
    def apply(request: Request, service: Service[RequestWithCredentials, Response]): Future[Response] = {
      val requestWithCredentials = new RequestWrapper with RequestWithCredentials {
        val underlying = request
        val credentials = credentialsParser(request) getOrElse NullCredentials
      }

      service(requestWithCredentials)
    }
  }

  //Note how the underlying service requires an authenticated request,
  //and that this is statically verified. Filters can thus be thought
  //of as service transformers. Many filters can be composed together:
  val upFilter =
  logTransaction     andThen
  handleExceptions   andThen
  extractCredentials andThen
  homeUser           andThen
  authenticate       andThen
  route
}
*/