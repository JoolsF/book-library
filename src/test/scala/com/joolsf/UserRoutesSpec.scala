package com.joolsf

import com.joolsf.http.JsonSupport
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ FlatSpec, Matchers }

class LibraryRoutesSpec extends FlatSpec with Matchers with ScalaFutures with JsonSupport {

  it should "foo" in {
    succeed
  }

}