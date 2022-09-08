package testing

/** 
 * @return the sum of x and y
*/
def add(x: Int, y: Int): Int = x + y

/**
  * @return the n-th Fibonacci's number
  * (the first one is 0)
  */
def fibonacci(n: Int): Int = 
  assert(n >= 0)
  n match
    case 0 | 1 => 1
    case x     => fibonacci(n - 2) + fibonacci(n - 1)
