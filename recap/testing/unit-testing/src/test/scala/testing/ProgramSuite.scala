package testing

class ProgramSuite extends munit.FunSuite {
  test("add") {
    val obtained = add(1, 1)    
    assertEquals(obtained, 2)
  }
  test("fibonacci") {
    assertEquals(fibonacci(0), 1)
    assertEquals(fibonacci(1), 1)
    assertEquals(fibonacci(2), 2)
    assertEquals(fibonacci(3), 3)
    assertEquals(fibonacci(4), 5)
    assertEquals(fibonacci(5), 8)
    assertEquals(fibonacci(6), 13)
  }
}