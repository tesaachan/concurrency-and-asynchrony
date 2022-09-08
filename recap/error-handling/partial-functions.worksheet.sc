def somePartialFunction: PartialFunction[Int, Char] =
  case n if n >= 0 && n <= 9 => 
    (n + '0').toChar

somePartialFunction.isDefinedAt(5)
somePartialFunction.isDefinedAt(11)

val someValue = 9
if somePartialFunction.isDefinedAt(someValue) then
  somePartialFunction(someValue)


// Partial functions translation:

val handler: PartialFunction[Throwable, Unit] =
  case exn: RuntimeException => println("An exception was thrown")

// 'handler' is the same as 'fullHandler':

val fullHandler: PartialFunction[Throwable, Unit] =
  new PartialFunction[Throwable, Unit]:
    def apply(t: Throwable): Unit = t match
      case exn: RuntimeException => println("An exception was thrown")
    def isDefinedAt(t: Throwable): Boolean = t match
      case exn: RuntimeException => true
      case _ => false
