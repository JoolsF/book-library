package com.joolsf.http

import com.joolsf.service.Services
import akka.http.scaladsl.server.{ AuthorizationFailedRejection, Directive1, Directives, Route }

trait RouteItem extends Directives {

  import RouteItem._
  val services: Services

  def ping: Route = path("ping")(get(complete("pong")))

  def routes: Route

  def withLibraryUserName: Directive1[String] = headerValueByName(LibaryUserTokenName)

  def libraryUserValidationWithToken(): Directive1[Long] =
    withLibraryUserName.flatMap { token =>
      if (token == LibraryUserTokenTestValue) {
        provide(1) //test value
      } else {
        reject(AuthorizationFailedRejection)
      }
    }
}

object RouteItem {
  val LibaryUserTokenName = "X-Library-User-Token"
  val LibraryUserTokenTestValue = "foo" // test value
}
