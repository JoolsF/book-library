package com.joolsf.db

import java.time.LocalDate

import com.joolsf.entities._

import scala.collection.mutable.ListBuffer
import scala.concurrent.{ ExecutionContext, Future }

/**
 * Toy example to simulate a persistence layer
 */
class LibraryRepository() {

  private var autoInc = {
    var i = 0
    () => i = i + 1; i
  }

  private val employees: ListBuffer[Employee] = ListBuffer()
  private val books: ListBuffer[Book] = ListBuffer()
  private val loans: ListBuffer[Loan] = ListBuffer()

  // books
  def addBook(name: String)(implicit ec: ExecutionContext): Future[Int] = Future {
    val id = autoInc()
    books += Book(id, name, 1)
    id
  }

  def getBook(id: Long)(implicit executionContext: ExecutionContext): Future[Option[Book]] = Future {
    books.find(_.id == id)
  }

  def getBook(name: String)(implicit executionContext: ExecutionContext): Future[Option[Book]] = Future {
    books.find(_.title == name)
  }

  def incrementBookNumber(book: Book)(implicit executionContext: ExecutionContext): Future[Int] = Future {
    books -= book
    books += book.copy(numberOfCopies = book.numberOfCopies + 1)
    1
  }

  def decrementBookNumber(book: Book)(implicit executionContext: ExecutionContext): Future[Int] = Future {
    books -= book
    books += book.copy(numberOfCopies = book.numberOfCopies - 1)
    1
  }

  def deleteBook(book: Book)(implicit ec: ExecutionContext): Future[Int] = Future {
    books -= book
    1
  }

  // employees
  def addEmployee(name: String)(implicit executionContext: ExecutionContext): Future[Int] = Future {
    val id = autoInc()
    employees += Employee(id, name)
    id
  }

  def getEmployee(name: String)(implicit executionContext: ExecutionContext): Future[Option[Employee]] = Future {
    employees.find(_.name == name)
  }

  def getEmployee(id: Long)(implicit executionContext: ExecutionContext): Future[Option[Employee]] = Future {
    employees.find(_.id == id)
  }

  //loans
  def addLoan(
    book: Book,
    employee: Employee,
    loanDate: LocalDate,
    returnDate: LocalDate)(implicit executionContext: ExecutionContext): Future[Int] = Future {
    val id = autoInc()
    loans += Loan(id, book, employee, loanDate, returnDate)
    id
  }

}
