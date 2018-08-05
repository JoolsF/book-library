package com.joolsf.http

import akka.http.scaladsl.model.{ StatusCode, StatusCodes }
import akka.http.scaladsl.server.Route
import com.joolsf.entities.{ Book, BookRequest, EmployeeRequest }

import scala.concurrent.{ ExecutionContext, Future }

trait LibraryRouting extends RouteItem with JsonSupport {

  override def routes: Route = extractExecutionContext { implicit ec =>
    pathPrefix("library") {
      ping ~
        libraryUserValidationWithToken() { _ =>
          concat(
            booksRoutes(),
            employeeRoutes())
        }
    }
  }

  def booksRoutes()(implicit ec: ExecutionContext) = pathPrefix("books") {
    post {
      entity(as[BookRequest]) { bookRequest =>
        onSuccess(services.libraryService.addBook(bookRequest)) { res =>
          complete(StatusCodes.Created, res)
        }
      }
    }
  }

  def employeeRoutes()(implicit ec: ExecutionContext) = pathPrefix("employees") {
    post {
      entity(as[EmployeeRequest]) { employeeRequest =>
        onSuccess(services.libraryService.addEmployee(employeeRequest)) {
          case Some(id) => complete(StatusCodes.Created, id.toString) //TODO fix toString
          case None => complete(StatusCodes.Conflict)
        }
      }
    }
  }

}
