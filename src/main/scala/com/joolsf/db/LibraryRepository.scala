package com.joolsf.db

import com.joolsf.entities.{ Book, BookRequest, Employee, Loan }

import scala.collection.mutable.ListBuffer
import scala.concurrent.{ ExecutionContext, Future }

/**
 * Toy example to simulate a pesistence layer
 */
class LibraryRepository() {

  private var autoInc = {
    var i = 0
    () => i = i + 1; i
  }

  private val employees: ListBuffer[Employee] = ListBuffer()
  private val books: ListBuffer[Book] = ListBuffer()
  private val loans: ListBuffer[Loan] = ListBuffer()

  def getBook(name: String)(implicit executionContext: ExecutionContext): Future[Option[Book]] = Future {
    books.find(_.title == name)
  }

  def updateBookNumber(book: Book)(implicit executionContext: ExecutionContext): Future[Int] = Future {
    books -= book
    books += book.copy(numberOfCopies = book.numberOfCopies + 1)
    1
  }

  def deleteBook(book: Book)(implicit ec: ExecutionContext): Future[Int] = Future {
    books -= book
    1
  }

  def addBook(name: String)(implicit ec: ExecutionContext): Future[Int] = Future {
    books += Book(autoInc(), name, 1)
    1
  }

}
