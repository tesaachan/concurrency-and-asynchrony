package jvm

object Utils {
  def thread(body: => Unit): Thread = {
    val t = new Thread {
      override def run() = body
    }
    t.start()
    t
  }

  def log(s: String): Unit = {
    val t: Thread = Thread.currentThread
    println(s"[${t.getName}] $s")
  }
}
