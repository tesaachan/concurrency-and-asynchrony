// Turn callback into simplistic Future type

class TurnCallbackIntoFuture[A, B]:

  def program1(a: A, k: B => Unit): Unit

  // curry the continuation parameter
  def program2(a: A): (B => Unit) => Unit

  // introduce a type alias
  type Future[+T] = (T => Unit) => Unit
  def program3(a: A): Future[B]

  // add failure handling
  type FutureWithTry[+T] = (Try[T] => Unit) => Unit


// Towards a brighter Future

trait Future[+T] extends ((Try[T] => Unit) => Unit):
    def apply(body: => T): Future[T]
    def onComplete(k: Try[T] => Unit): Unit
