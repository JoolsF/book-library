package com.joolsf.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.joolsf.entities._

trait JsonSupport extends SprayJsonSupport {

  import spray.json.DefaultJsonProtocol._

  implicit val bookRequestJsonFormat = jsonFormat1(BookRequest)
  implicit val bookJsonFormat = jsonFormat3(Book)

  implicit val employeeRequestJsonFormat = jsonFormat1(EmployeeRequest)

  implicit val loanRequestJsonFormat = jsonFormat2(LoanRequest)

}

