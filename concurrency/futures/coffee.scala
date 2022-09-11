import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


def chatWithColleagues(): Future[Unit] = ???
def makeCoffee(): Future[Unit] = ???
def drink(): Future[Unit] = ???

def coffeeBreak(): Future[Unit] =
  val eventuallyCoffeeDrunk = makeCoffee().flatMap(drink)
  val eventuallyChatted     = chatWithColleagues()
  eventuallyCoffeeDrunk.zip(eventuallyChatted)
    .map(_ => ())


def coffeeProcess(coffeeNum: Int) =
  for
    _ <- Future(println(s"make coffee $coffeeNum"))
    _ <- Future(println(s"drink coffee $coffeeNum"))
    _ <- Future(println(s"chat after coffee $coffeeNum"))
  yield ()

@main def main =
  println("exec:")
  coffeeProcess(1)
  coffeeProcess(2)
  coffeeProcess(3)
