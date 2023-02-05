package jvm

import Utils._

object ThreadMain {
  val t: Thread = Thread.currentThread
  val name = t.getName
  println(s"I am the thread $name")
}

object ThreadsCreation {
  class MyThread extends Thread {
    override def run(): Unit = println("New thread running.")
  }
  val t = new MyThread
  t.start()
  t.join()
  println("New thread joined.")
}

object ThreadsSleep {
  val t = thread {
    Thread.sleep(1000)
    log("New thread running")
    Thread.sleep(1000)
    log("Still running.")
    Thread.sleep(1000)
    log("Completed")
  }
  t.join()
  log("New thread joined.")
}

object ThreadsNondeterminism {
  val t = thread { log("New thread running.") }
  log("...")
  log("...")
  t.join()
  log("New thread joined.")
}

object ThreadsCommunicate {
  var result: String = null
  val t = thread { result = "\nTitle\n" + "=" * 5 }
  t.join()
  log(result)
}

object ThreadsProtectedUid {
  var uidCount = 0L
  def getUniqueId = this.synchronized {
    val freshUid = uidCount + 1
    uidCount = freshUid
    freshUid
  }
}
