import java.io.InputStream
import scala.util.control.NonFatal.apply

def parseIntData(x: InputStream) = ???
def attemptSomething() = ???

@main def main =
  val stream = getClass.getResourceAsStream("data.txt")
  val data = 
    try
      parseIntData(stream)
    catch
      case NonFatal(exn) => -1
      // You MUST NOT catch all Throwable, because some of them are FATAL
      // Instead use NonFatal extractor
      finally
        stream.close()

    try
      attemptSomething()
    catch
      case exn: ArithmeticException => 
        println("An ArithmeticException occurred")
      case exn: RuntimeException => 
        println("An RuntimeException occurred")
end main