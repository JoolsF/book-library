package com.joolsf.http

import akka.http.scaladsl.server.Route

trait LibraryRouting extends RouteItem with JsonSupport {
  override def routes: Route = extractExecutionContext { implicit ec =>
    pathPrefix("library") {
      ping
    }
  }

}
