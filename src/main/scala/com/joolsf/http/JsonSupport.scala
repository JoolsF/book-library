package com.joolsf.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import com.joolsf.entities.{ Book, BookRequest }

trait JsonSupport extends SprayJsonSupport {
  // import the default encoders for primitive types (Int, String, Lists etc)

  //  implicit val userJsonFormat = jsonFormat3(User)
  //  implicit val usersJsonFormat = jsonFormat1(Users)
  //
  //  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)

  import spray.json.DefaultJsonProtocol._

  implicit val bookRequestJsonFormat = jsonFormat1(BookRequest)
  implicit val bookJsonFormat = jsonFormat3(Book)
}

