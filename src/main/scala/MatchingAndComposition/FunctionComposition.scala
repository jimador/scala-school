package matchingandcomposition

/**
  * Created by james on 5/17/2016.
  */
object FunctionComposition {
  //Scala provides 'compose' and 'andThem
  def f(s: String) = "f(" + s + ")"
  //f: (String)java.lang.String
  def g(s: String) = "g(" + s + ")"
  //g: (String)java.lang.String

  val fComposeG = f _ compose g _ //the '_' on g is not needed
  fComposeG("yay")
  //res0: java.lang.String = f(g(yay))

  val fAndThenG = f _ andThen g
  //fAndThenG: (String) => java.lang.String = <function>

  fAndThenG("yay")
  //res1: java.lang.String = g(f(yay))
}
