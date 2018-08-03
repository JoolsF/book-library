package com.joolsf.http

import akka.http.scaladsl.model.{ StatusCode, StatusCodes }
import akka.http.scaladsl.server.Route
import com.joolsf.entities.{ Book, BookRequest }

import scala.concurrent.Future

trait LibraryRouting extends RouteItem with JsonSupport {

  override def routes: Route = extractExecutionContext { implicit ec =>
    pathPrefix("library") {
      ping ~
        pathPrefix("books") {
          post {
            entity(as[BookRequest]) { bookRequest =>
              onSuccess(services.libraryService.addBook(bookRequest)) { res =>
                complete(StatusCodes.OK, res)
              }
            }
          }
        }
    }
  }

}
