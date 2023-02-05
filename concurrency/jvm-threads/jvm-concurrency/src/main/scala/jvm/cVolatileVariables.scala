package jvm

import jvm.Utils._

// Volatile variables

// They support atomic reading and writing and usually used for condition flags.

// Advantages:
//  - reading and writing can not be reordered within a thread
//  - changes of volatile variables got caught by other thread immediately

class Page(val txt: String, var position: Int)
object Volatile {
  val pages = for (i <- 1 to 5) yield
    new Page("Na" * (100 - 20 * i) + " Batman!", -1)

  @volatile var found = false
  for (p <- pages) yield thread {
    var i = 0
    while (i < p.txt.length && !found)
      if (p.txt(i) == '!') {
        p.position = i
        found = true
      } else i += 1
  }
  while (!found) {}
  log(s"results: ${pages.map(_.position)}")
}

// Sequence of several readings and writings of volatile variables
// without additional synchronizations is executed NOT ATOMICALLY

// for example, volatiles cannot implement getUniqueId.
