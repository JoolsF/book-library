package com.joolsf.http

import com.joolsf.entities.{ Book, BookRequest, EmployeeRequest, LoanRequest }
import com.joolsf.service.{ LibraryService, Services }
import java.time.LocalDate
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{ ContentTypes, MessageEntity, StatusCodes }
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.mockito.Mockito.when
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{ FlatSpec, Matchers }
import io.circe.generic.auto._

import scala.concurrent.Future

class LibraryRoutingTest extends FlatSpec with Matchers with ScalaFutures with JsonSupport with ScalatestRouteTest with MockitoSugar {
  val libraryUserToken = RawHeader(RouteItem.LibaryUserTokenName, "foo")
  val libraryUserTokenWrong = RawHeader(RouteItem.LibaryUserTokenName, "bar")
  val requestHeaders = List(libraryUserToken)
  val requestHeadersWrong = List(libraryUserTokenWrong)

  val mockLibraryService = mock[LibraryService]
  implicit val routes = new LibraryRouting {
    override val services: Services = Services(mockLibraryService)
  }.routes

  it should "return forbidden if an invalid user token is sent" in {
    val bookRequest = BookRequest("book one")
    val bookEntity = Marshal(bookRequest).to[MessageEntity].futureValue

    val request = Post("/library/books").withEntity(bookEntity).withHeaders(requestHeadersWrong)

    request ~> Route.seal(routes) ~> check {
      status should ===(StatusCodes.Forbidden)
    }
  }

  it should "add a new book to the library" in {
    val bookRequest = BookRequest("book one")
    val bookEntity = Marshal(bookRequest).to[MessageEntity].futureValue

    when(mockLibraryService.addBook(bookRequest)).thenReturn(Future.successful(Some(Book(1, "book one", 1))))

    val request = Post("/library/books").withEntity(bookEntity).withHeaders(requestHeaders)

    request ~> routes ~> check {
      status should ===(StatusCodes.Created)

      contentType should ===(ContentTypes.`application/json`)

      entityAs[Book] shouldBe Book(1, "book one", 1)
    }
  }

  it should "add a new employee to the library" in {
    val employeeRequest = EmployeeRequest("Joe Bloggs")
    val employeeEntity = Marshal(employeeRequest).to[MessageEntity].futureValue

    when(mockLibraryService.addEmployee(employeeRequest)).thenReturn(Future.successful(Some(1)))

    val request = Post("/library/employees").withEntity(employeeEntity).withHeaders(requestHeaders)

    request ~> routes ~> check {
      status should ===(StatusCodes.Created)

      contentType should ===(ContentTypes.`application/json`)

      entityAs[String] should ===("1")
    }
  }

  it should "add a new loan to the library" in {
    val loanRequest = LoanRequest(1, 2)
    val loanEntity = Marshal(loanRequest).to[MessageEntity].futureValue
    val today = LocalDate.now

    when(mockLibraryService.addLoan(loanRequest)).thenReturn(Future.successful(Right(today)))

    val request = Post("/library/loans").withEntity(loanEntity).withHeaders(requestHeaders)

    request ~> routes ~> check {
      status should ===(StatusCodes.Created)

      contentType should ===(ContentTypes.`application/json`)

      entityAs[String] should ===(today.toString)
    }
  }

}
