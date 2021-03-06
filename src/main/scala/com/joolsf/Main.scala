package com.joolsf

import com.joolsf.db.LibraryRepository
import com.joolsf.http.LibraryRouting
import com.joolsf.service.{ LibraryService, Services }
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

object QuickstartServer extends App with LibraryRouting {

  implicit val system: ActorSystem = ActorSystem("helloAkkaHttpServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  override val services = new Services(
    new LibraryService(
      new LibraryRepository()))

  Http().bindAndHandle(routes, "localhost", 8080)

  println(s"Server online at http://localhost:8080/")

  Await.result(system.whenTerminated, Duration.Inf)

}

