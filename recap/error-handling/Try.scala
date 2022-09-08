// class Try

import scala.util.Try
import javax.management.RuntimeErrorException

def attemptSomething(): Try[Unit] =
  Try {
    println("So far, so good")
    println("Still there")
    throw RuntimeException("We can't continue")
    println("You will never see this")
  }

// @main def main =
//   println(attemptSomething())
//   println("I'm here")

@main def main =
  attemptSomething().recover {
  // recover is like 'catch' in a try expression
  case exn: RuntimeException => 
    System.err.println(s"Something went wrong")
    println("Stopping the program")
  }

import java.time.LocalDate
import scala.util.{Success, Failure}

def parseDate(str: String): Try[LocalDate] = 
  Try(LocalDate.parse(str))
    
val parsedDate = 
  parseDate("2020-02-02") match
  case Success(date)      => "Parced date"
  case Failure(throwable) => "Failed to parse date"


// Manipulating Try values

trait TRY[A]:

  def map[B](f: A => B): TRY[B]

  def flatMap[B](f: A => TRY[B]): TRY[B]

  def recover(f: PartialFunction[Throwable, A]): TRY[A]

  def recoverWith(f: PartialFunction[Throwable, TRY[A]]): TRY[A]

end TRY


import java.time.Period

def tryPeriod(str1: String, str2: String): Try[Period] =
  for
    date1 <- parseDate(str1)
    date2 <- parseDate(str2)
  yield
    Period.between(date1, date2)

val periods: List[Try[Period]] = List (
  tryPeriod("2020-07-28", "2021-07-05"),
  tryPeriod("2020-07-28", "2021-27-05"),
  tryPeriod("2020-07-28", "2021-07-55"),
  tryPeriod("202o-07-28", "2021-07-05")
)


// Parsing dates

import scala.io.Source
import scala.util.Using

def readDateStrings(filename: String): Try[Seq[String]] =
  // Using lets use some resource and release it when the block ends
  // Exactly like 'finally source.close()' in a try expression
  Using(Source.fromFile(filename)) { source =>
    source.getLines.toSeq
  }

def parseDates(filename: String): Try[Seq[LocalDate]] =
  readDateStrings(filename).flatMap { (dateStrings: Seq[String]) =>
    dateStrings.foldLeft[Try[Seq[LocalDate]]](Success(Vector.empty)) {
      (tryDates, dateString) =>
        for
          dates <- tryDates
          date  <- parseDate(dateString)
        yield
          dates :+ date
    }
  }

val datesFromFile = parseDates("/Users/georgy/Desktop/study/vii-semester/" +
  "concurrency-and-parallelism/recap/error-handling/dates.txt")
