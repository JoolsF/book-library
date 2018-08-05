package com.jools.service

import com.joolsf.db.LibraryRepository
import com.joolsf.entities.BookRequest
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
    val request = libraryService.addBook(bookRequest).flatMap(_ => libraryService.addBook(bookRequest))

    whenReady(request) { result =>
      result.isDefined shouldBe true
      result.get.title shouldBe bookTitle
      result.get.numberOfCopies shouldEqual 2
    }

  }

}
