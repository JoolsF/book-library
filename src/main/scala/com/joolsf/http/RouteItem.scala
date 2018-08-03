package com.joolsf.http

import akka.http.scaladsl.server.{ Directives, Route }
import com.joolsf.service.Services

trait RouteItem extends Directives {
  val services: Services

  def ping: Route = path("ping")(get(complete("pong")))

  def routes: Route
}
