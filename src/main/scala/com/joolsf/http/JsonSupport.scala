package com.joolsf.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

trait JsonSupport extends SprayJsonSupport {
  // import the default encoders for primitive types (Int, String, Lists etc)

  //  implicit val userJsonFormat = jsonFormat3(User)
  //  implicit val usersJsonFormat = jsonFormat1(Users)
  //
  //  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)
}

