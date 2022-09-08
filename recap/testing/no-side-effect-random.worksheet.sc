class Generator(prev: Int):
  def nextInt: (Int, Generator) =
    val result = prev * 22_667_228 + 1
    (result, Generator(result))

  def between(x: Int, y: Int) =
    val min = math.min(x, y)
    val delta = math.abs(x - y)
    val (result, nextGen) = nextInt
    (min + (result % delta), nextGen)

end Generator

object Generator:
  def init = Generator(42)

val gen1 = Generator.init
val (x, gen2) = gen1.nextInt
val (y, _) = gen1.nextInt
val (z, _) = gen2.nextInt
val (a, gen3) = Generator.init.between(5, 10)
val (b, _) = gen3.between(5, 10)

def f(a: F) = 5
class M(val x: Int)
class F(val c: Int, val y: Int) extends M(c)

