package com.joolsf.http

import akka.http.scaladsl.server.{ Directives, Route }

trait RouteItem extends Directives {
  def ping: Route = path("ping")(get(complete("pong")))

  def routes: Route
}
