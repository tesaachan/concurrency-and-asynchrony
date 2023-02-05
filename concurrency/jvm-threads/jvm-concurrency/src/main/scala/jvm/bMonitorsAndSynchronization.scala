package jvm

import scala.collection._
import Utils._

object SynchronizedNesting {
  private val transfers = mutable.ArrayBuffer[String]()
  def logTransfer(name: String, n: Int) = transfers.synchronized {
    transfers += s"transfer to account '$name' = $n"
  }

  class Account(val name: String, var money: Int)

  def add(account: Account, n: Int) = account.synchronized {
    account.money += n
    if (n > 10) logTransfer(account.name, n)
  }

  val jane = new Account("Jane", 100)
  val john = new Account("John", 200)
  val t1 = thread { add(jane, 5) }
  val t2 = thread { add(john, 50) }
  val t3 = thread { add(jane, 70) }
  t1.join(); t2.join(); t3.join()
  log(s"--- transfers ---\n$transfers")
}

object SynchronizedDeadlock {
  import SynchronizedNesting.Account
  def send(a: Account, b: Account, n: Int) = a.synchronized {
    b.synchronized {
      a.money -= n
      b.money += n
    }
  }

  val a = new Account("Jack", 1000)
  val b = new Account("Jill", 2000)
  val t1 = thread { for (i <- 0 until 100) send(a, b, 1) }
  val t2 = thread { for (i <- 0 until 100) send(b, a, 1) }
  t1.join(); t2.join()
  log(s"a = ${a.money}, b = ${b.money}")
}

// remember: if resources always get blocked in the same order => no deadlock will happen
// define the order of resource blocking to guarantee deadlock absence

object SynchronizedNoDeadlock {
  import ThreadsProtectedUid.getUniqueId

  class Account(val name: String, var money: Int) {
    val uid = getUniqueId
  }

  def send(a1: Account, a2: Account, n: Int) = {
    def adjust() = {
      a1.money -= n
      a2.money += n
    }
    if (a1.uid < a2.uid)
      a1.synchronized { a2.synchronized { adjust() } }
    else a2.synchronized { a1.synchronized { adjust() } }
  }

  val a = new Account("Jack", 1000)
  val b = new Account("Jill", 2000)
  val t1 = thread { for (i <- 0 until 100) send(a, b, 1) }
  val t2 = thread { for (i <- 0 until 100) send(b, a, 1) }
  t1.join(); t2.join()
  log(s"a = ${a.money}, b = ${b.money}")
}

// creation of a new thread is much more expensive than creation of a light object such as Account
// when tasks are too many => reusing of already existing threads is the solution

// this is called Thread Pool model

// active waiting in run. too bad
object SynchronizedBadPool {
  private val tasks = mutable.Queue[() => Unit]()

  val worker = new Thread {
    def poll(): Option[() => Unit] = tasks.synchronized {
      if (tasks.nonEmpty) Some(tasks.dequeue()) else None
    }

    override def run() = while (true) poll() match {
      case Some(task) => task()
      case None =>
    }
  }

  worker.setName("Worker")
  worker.setDaemon(true)
  worker.start()

  def asynchronous(body: => Unit) = tasks.synchronized {
    tasks.enqueue(() => body)
  }

  asynchronous { log("Hello ") }
  asynchronous { log("world!") }
  Thread.sleep(5000)
}

// if a thread holds object x then you can use wait and notify.
// when a thread T calls wait, it releases x's monitor and turns off awaiting,
// until another thread S calls notify of x.

// generally such S prepares some data for T

object SynchronizedGuardedBlocks {
  val lock = new AnyRef
  var message: Option[String] = None
  val greater = thread {
    lock.synchronized {
      while (message.isEmpty) lock.wait()
      log(message.get)
    }
  }
  lock.synchronized {
    message = Some("Hello")
    lock.notify()
  }
  greater.join()
}

object SynchronizedPool {
  private val tasks = mutable.Queue[() => Unit]()

  object Worker extends Thread {
    setDaemon(true)
    def poll() = tasks.synchronized {
      while (tasks.isEmpty) tasks.wait()
      tasks.dequeue()
    }
    override def run() = while (true) {
      val task = poll()
      task()
    }
  }

  Worker.start()
  def asynchronous(body: => Unit) = tasks.synchronized {
    tasks.enqueue(() => body)
    tasks.notify()
  }
  asynchronous { log("Hello ") }
  asynchronous { log("world!") }
  Thread.sleep(500)
}

// Worker above has took stack memory while waiting unused.
// To free stack and interrupt a thread use the method .interrupt.

// if a thread is waiting: .interrupt calls an exception InterruptedException in a thread
// if a thread is active:  .interrupt sets its flag interrupt without exception to be checked by .isInterrupted

// There is a pattern "graceful shutdown":
// A thread sets a condition for terminating, and then calls notify to continue a waiting thread.
// When the condition is met the working thread must free resources and terminate.

object SynchronizedPool_v2 {
  private val tasks = mutable.Queue[() => Unit]()

  object Worker extends Thread {
    var terminated = false

    def poll(): Option[() => Unit] = tasks.synchronized {
      while (tasks.isEmpty && !terminated) tasks.wait()
      if (!terminated) Some(tasks.dequeue()) else None
    }
    @scala.annotation.tailrec
    override def run() = poll() match {
      case Some(task) => task(); run()
      case None =>
    }
    def shutdown() = tasks.synchronized {
      terminated = true
      tasks.notify()
    }
  }

  // now Main thread can call Worker's method .shutdown to terminate a thread.
  // Use "graceful shutdown" pattern to guarantee terminating of threads without race conditions and interruptions
}

// .interrupt is suitable when there is no possibility or reason to continue a thread by .notify
