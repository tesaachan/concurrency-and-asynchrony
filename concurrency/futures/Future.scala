import scala.concurrent.Future

// Future API without implicits
trait FUTURE[A]:
  def map[B](f: A => B): Future[B]
  def flatMap[B](f: A => Future[B]): Future[B]
  def zip[B](that: Future[B]): Future[(A, B)]
  def recover(f: Throwable => A): Future[A]
  def recoverWith(f: Throwable => Future[A]): Future[A]

object FUTURE:
 def traverse[A, B](as: Seq[A])(f: A => Future[B]): Future[Seq[B]] = ???


import scala.concurrent.ExecutionContext.Implicits._
import scala.util.Random
import scala.util.control.NonFatal

def getPagesCount() = Future(42)

// tries to get once or fails
def getPage(page: Int): Future[String] =
  if Random.nextDouble() > 0.95 then
    Future.failed(Exception(s"Timeout when fetching page $page"))
  else Future(s"Page $page")

//at most 3 times to retry
def resilientGetPage(page: Int): Future[String] =
  val maxAttempts = 3
  def attempt(remainingAttempts: Int): Future[String] =
    if remainingAttempts == 0 then
      Future.failed(Exception(s"Failed after $maxAttempts attempts"))
    else
      println(s"Trying to fetch page $page ($remainingAttempts remaining attempts)")
      getPage(page).recoverWith { case NonFatal(_) =>
        System.err.println(s"Fetching page $page failed...")
        attempt(remainingAttempts - 1)
      }
  attempt(maxAttempts)

// parallel fetch of all pages
def getAllPages(): Future[Seq[String]] =
  getPagesCount().flatMap { pagesCount =>
    Future.traverse(1 to pagesCount)(getPage)
  }

// sequential fetch of all pages
def getAllPagesSequentially(): Future[Seq[String]] =
  getPagesCount().flatMap { pagesCount =>
    val allPages = 1 to pagesCount
    allPages.foldLeft[Future[Seq[String]]](Future.successful(Vector.empty)) {
      (eventualPreviousPages, pageNumber) =>
        eventualPreviousPages.flatMap { previousPages =>
          getPage(pageNumber)
            .map(pageContent => previousPages :+ pageContent)
        }
    }
  }

@main def main =
  getAllPages().onComplete(println)

// Just advice: 
// combine Futures with zip instead of flatMap to maximize parallelism
