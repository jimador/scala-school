package concurrency

/**
  * Created by james on 5/20/2016.
  */
object Concurrency {
  /**
    * Runnable/Callable -- both are traits
    *
    * trait Runnable {
    * def run(): Unit
    * }
    *
    * trait Callable[V] {
    *   def call(): V
    * }
    */


  /**
    * Threads -- built on top of Java threads
    */
  val hello = new Thread(new Runnable {
    override def run(): Unit = println("hello world")
  })

  //just like Java, call start on hello

  /**
    * More concurrency from Java
    *   - Executors
    *   - Future
    *   - synchronized
    *   - volatile
    *   - AtomicReference
    *   - CountDownLatch
    *   - AtomicInteger/Long
    *   - ReadWriteLock
    *   - SynchronizedMap
    *   - ConcurrentHashMap
    *
    */

  /**
    *
    */
}
