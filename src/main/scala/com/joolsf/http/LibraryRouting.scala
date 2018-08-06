package com.joolsf.http

import com.joolsf.entities._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import scala.concurrent.ExecutionContext
import io.circe.generic.auto._

trait LibraryRouting extends RouteItem with FailFastCirceSupport with JsonSupport {

  override def routes: Route = extractExecutionContext { implicit ec =>
    pathPrefix("library") {
      ping ~
        libraryUserValidationWithToken() { _ =>
          concat(
            booksRoutes(),
            employeesRoutes(),
            loansRoutes())
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

  def employeesRoutes()(implicit ec: ExecutionContext) = pathPrefix("employees") {
    pathPrefix(IntNumber) { employeeId =>
      (pathPrefix("loans") & pathEndOrSingleSlash) {
        get {
          onSuccess(services.libraryService.getEmployeesLoans(employeeId)) {
            case Right(res) => complete(StatusCodes.OK, res)
            case Left(_: NotFoundError) => complete(StatusCodes.NotFound)
            case _ => complete(StatusCodes.InternalServerError)
          }
        }
      }
    } ~
      post {
        entity(as[EmployeeRequest]) { employeeRequest =>
          onSuccess(services.libraryService.addEmployee(employeeRequest)) {
            case Some(id) => complete(StatusCodes.Created, id.toString)
            case None => complete(StatusCodes.Conflict)
          }
        }
      }
  }

  def loansRoutes()(implicit ec: ExecutionContext) = pathPrefix("loans") {
    post {
      entity(as[LoanRequest]) { loanRequest =>
        onSuccess(services.libraryService.addLoan(loanRequest)) {
          case Right(returnDate) => complete(StatusCodes.Created, returnDate.toString)
          case Left(_: NotFoundError) => complete(StatusCodes.NotFound)
          case Left(_: BookUnavailable) => complete(StatusCodes.Conflict)
          case _ => complete(StatusCodes.InternalServerError)
        }
      }
    }
  }

}
