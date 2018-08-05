package com.jools.service

import java.time.LocalDate

import com.joolsf.db.LibraryRepository
import com.joolsf.entities._
import com.joolsf.http.JsonSupport
import com.joolsf.service.LibraryService
import org.scalatest.{ FlatSpec, Matchers }
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.ExecutionContext.Implicits.global

class LibraryServiceTest extends FlatSpec with Matchers with ScalaFutures with JsonSupport with MockitoSugar {

  val libraryService = new LibraryService(
    new LibraryRepository //Note - not mocking repository database dependency as its a fake db anyway.
  )

  it should "add a book to the library" in {
    val bookTitle = "Test book one"
    val bookRequest = BookRequest(bookTitle)
    val request = libraryService.addBook(bookRequest)

    whenReady(request) { result =>
      result.isDefined shouldBe true
      result.get.title shouldEqual bookTitle
      result.get.numberOfCopies shouldEqual 1
    }

  }

  it should "add multiple books to the library" in {
    val bookTitle = "Test book two"
    val bookRequest = BookRequest(bookTitle)

    def request = libraryService.addBook(bookRequest).flatMap(_ => libraryService.addBook(bookRequest))

    whenReady(request) { result =>
      result.isDefined shouldBe true
      result.get.title shouldBe bookTitle
      result.get.numberOfCopies shouldEqual 2
    }

  }

  it should "add an employee to the system" in {
    val request = libraryService.addEmployee(EmployeeRequest("Joe Bloggs"))
    whenReady(request) { result =>
      result.isDefined shouldBe true
    }

  }

  it should "should not an employee to the system if one already exists with the same name" in {
    val employeeRequest = EmployeeRequest("Duplicate employee")
    val request = libraryService.addEmployee(employeeRequest).flatMap(_ => libraryService.addEmployee(employeeRequest))
    whenReady(request) { result =>
      result.isDefined shouldBe false
    }

  }

  it should "add a loan to the system" in {
    val book = "book 1"
    val employee = "employee 1"

    val request = for {
      book <- libraryService.addBook(BookRequest(book))
      employee <- libraryService.addEmployee(EmployeeRequest(employee))
      loan <- libraryService.addLoan(LoanRequest(book.get.id, employee.get))
    } yield loan

    whenReady(request) { result =>
      result.isRight shouldBe true
      result.right.get shouldBe a[LocalDate]
    }
  }

  it should "not add a loan to the system if the employee does not exist" in {
    val book = "book 2"

    val request = for {
      book <- libraryService.addBook(BookRequest(book))
      loan <- libraryService.addLoan(LoanRequest(book.get.id, 9999))
    } yield loan

    whenReady(request) { result =>
      result.isLeft shouldBe true
      result.left.get shouldBe NotFoundError("employee exists false book exists true")
    }
  }

  it should "not add a loan to the system if the book does not exist" in {
    val employee = "employee 2"

    val request = for {
      employee <- libraryService.addEmployee(EmployeeRequest(employee))
      loan <- libraryService.addLoan(LoanRequest(9999, employee.get))
    } yield loan

    whenReady(request) { result =>
      result.isLeft shouldBe true
      result.left.get shouldBe NotFoundError("employee exists true book exists false")
    }
  }

  it should "not add a loan to the system if no copies of the book are available" in {
    val book = "book 3"
    val employee = "employee 3"

    val request = for {
      book <- libraryService.addBook(BookRequest(book))
      employee <- libraryService.addEmployee(EmployeeRequest(employee))
      _ <- libraryService.addLoan(LoanRequest(book.get.id, employee.get))
      loan <- libraryService.addLoan(LoanRequest(book.get.id, employee.get))
    } yield loan

    whenReady(request) { result =>
      result.isLeft shouldBe true
      result.left.get shouldBe BookUnavailable("no copies of book left to loan")
    }
  }

}
