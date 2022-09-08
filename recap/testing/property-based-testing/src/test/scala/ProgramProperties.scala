package testing

import org.scalacheck.*
import org.scalacheck.Prop.*


/**
 * ScalaCheck User Guide:
 * https://github.com/typelevel/scalacheck/blob/main/doc/UserGuide.md
 * 
 */
class ProgramProperties extends munit.ScalaCheckSuite {

  val fibonacciDomain: Gen[Int] = Gen.choose(3, 1e3.toInt)

  property("fib(n) == fib(n-1) + fib(n-2)") {
    forAll(fibonacciDomain.suchThat(n => n >= 3)) { (n: Int) =>
      fibonacci(n) == fibonacci(n - 1) + fibonacci(n - 2)
    }

  property("all fib numbers are positive")
    forAll(fibonacciDomain) { (n: Int) =>
      fibonacci(n) >= 0
    }
  }
}
